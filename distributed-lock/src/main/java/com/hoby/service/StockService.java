package com.hoby.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hoby.lock.DistributedLockClient;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import com.hoby.entity.Stock;
import com.hoby.lock.RedisDistributedLock;
import com.hoby.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hoby
 * @since 2023-02-21
 */
@Slf4j
@Service
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StockService {

    @Resource
    private StockMapper stockMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private DistributedLockClient distributedLockClient;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 使用redisson
     */
    public void deductUsingRedisson() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();

        try {
            // 查询库存
            Integer stock = Convert.toInt(redisTemplate.opsForValue().get("stock"));
            // 判断库存数量
            if (stock != null && stock > 0) {
                // 扣减库存
                redisTemplate.opsForValue().decrement("stock");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * redis可重入分布式锁
     */
    public void deductRedisReentrantDistributedLock() {
        RedisDistributedLock lock = distributedLockClient.getRedisLock("lock");
        lock.lock();

        try {
            // 查询库存
            Integer stock = Convert.toInt(redisTemplate.opsForValue().get("stock"));
            // 判断库存数量
            if (stock != null && stock > 0) {
                // 扣减库存
                redisTemplate.opsForValue().decrement("stock");
            }

            this.testReenter();
        } finally {
            lock.unlock();
        }
    }

    public void testReenter() {
        RedisDistributedLock lock = distributedLockClient.getRedisLock("lock");
        lock.lock();
        System.out.println("test redis lock reenter");
        lock.unlock();
    }

    /**
     * redis分布式锁扣减库存
     */
    public void deductRedisDistributedLock() {
        String stockKey = "stock";
        String lockKey = "lock";
        String uuid = IdUtil.fastUUID();
        // 循环获取锁
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 10, TimeUnit.SECONDS))) {
            ThreadUtil.sleep(50);
        }
        try {
            // 查询库存
            Integer stock = Convert.toInt(redisTemplate.opsForValue().get(stockKey));
            // 判断库存数量
            if (stock != null && stock > 0) {
                // 扣减库存
                redisTemplate.opsForValue().decrement(stockKey);
            }
        } finally {
            // 解锁，判断UUID防止误删
            /*if (uuid.equals(Convert.toStr(redisTemplate.opsForValue().get(lockKey)))) {
                redisTemplate.delete(lockKey);
            }*/
            // 使用lua脚本，保证原子性
            String script =
                    "if redis.call('get', KEYS[1]) == ARGV[1] " +
                            "then " +
                            "   return redis.call('del', KEYS[1])" +
                            "else " +
                            "   return 0 " +
                            "end";
            redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockKey), uuid);
        }
    }

    /**
     * redis乐观锁扣减库存
     */
    public void deductRedisOptimisticLock() {
        String key = "stock";
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // watch
                operations.watch(key);
                // 查询库存
                Integer stock = Convert.toInt(operations.opsForValue().get(key));
                // 判断库存数量
                if (stock != null && stock > 0) {
                    // multi
                    operations.multi();
                    // 扣减库存
                    operations.opsForValue().decrement(key);
                    // exec
                    List exec = operations.exec();
                    // 如果执行事务返回的结果为空，表示执行失败
                    if (CollUtil.isEmpty(exec)) {
                        ThreadUtil.sleep(RandomUtil.randomLong(1, 20));
                        deductRedisOptimisticLock();
                    }
                }
                return null;
            }
        });
    }

    /**
     * mysql乐观锁扣减库存
     */
    public void deductOptimisticLock() {

        // 查询库存
        List<Stock> stocks = stockMapper.selectList(new QueryWrapper<Stock>().eq("product_code", "1001"));
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        // 取第一个库存
        Stock stock = stocks.get(0);
        // 判断库存数量
        if (stock.getCount() > 0) {
            // 记录版本号
            Integer version = stock.getVersion();
            // 扣减库存
            stock.setCount(stock.getCount() - 1);
            stock.setVersion(version + 1);
            int update = stockMapper.update(stock, new UpdateWrapper<Stock>()
                    .eq("id", stock.getId()).eq("version", version));
            if (update == 0) {
                ThreadUtil.sleep(RandomUtil.randomLong(1, 20));
                this.deductOptimisticLock();
            }
        }
    }

    /**
     * mysql悲观锁扣减库存
     */
    @Transactional
    public void deductPessimisticLock() {

        // 查询库存
        List<Stock> stocks = stockMapper.queryStock("1001");
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        // 取第一个库存
        Stock stock = stocks.get(0);
        // 判断库存数量
        if (stock.getCount() > 0) {
            // 扣减库存
            stock.setCount(stock.getCount() - 1);
            stockMapper.updateById(stock);
        }
    }

    /**
     * 一条sql扣减库存
     */
    public void deductWithOneSql() {
        this.stockMapper.deductStock(1L, 1);
    }

    /**
     * jvm本地锁扣减库存
     */
    public synchronized void deductJVMLock() {
        this.deduct();
    }

    public void deduct() {
        // 查询库存
        Stock stock = this.stockMapper.selectById(1L);

        // 判断库存数量
        if (stock != null && stock.getCount() > 0) {
            // 扣减库存
            stock.setCount(stock.getCount() - 1);
            this.stockMapper.updateById(stock);
        }
    }

}
