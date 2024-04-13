package com.hoby.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hoby
 * @since 2024-04-11
 */
public class QueryHelper {

    /**
     * 按照指定映射关系将列表转化为Map<hr/>
     * 用法：
     * <pre>
     * 
     * </pre>
     * 
     * @param data     原数据
     * @param function 映射函数
     * @param <K>      映射属性类型
     * @param <T>      数据类型
     * @return 映射结果
     */
    private static <K, T> Map<K, T> list2Map(List<T> data, Function<T, K> function) {
        return data.stream().collect(Collectors.toMap(function, item -> item, (k1, k2) -> k2));
    }

    /**
     * 分批查询<hr/>
     * 用法：
     * <pre>
     *     private void method(List<Long> userIds) {
     *
     *         // Before: 默认查询1w条数据，DB压力大
     *         List<User> usersByDefault = userMapper.queryUserByIds(userIds);
     *
     *         // After: 分批并行查询
     *         List<User> usersByBatch = batchQuery(userIds, 100, userMapper::queryUserByIds, null);
     *     }
     * </pre>
     *
     * @param keys      查询参数
     * @param size      每批的大小
     * @param queryFunc 查询函数
     * @param executor  并行调度器（不传时，取系统默认的 {@link ForkJoinPool#commonPool()}）
     * @param <K>       查询参数类型
     * @param <T>       数据类型
     * @return          查询结果
     */
    public static <K, T> List<T> batchQuery(List<K> keys, int size, Function<List<K>, List<T>> queryFunc, Executor executor) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be positive");
        }
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyList();
        }
        Executor actualExecutor = Optional.ofNullable(executor).orElseGet(ForkJoinPool::commonPool);
        List<CompletableFuture<List<T>>> futures = Lists.partition(keys, size).stream()
                .distinct()
                .map(batchKeys -> CompletableFuture.supplyAsync(() -> queryFunc.apply(batchKeys), actualExecutor))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    
}
