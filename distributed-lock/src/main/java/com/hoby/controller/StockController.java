package com.hoby.controller;

import com.hoby.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hoby
 * @since 2023-02-21
 */
@RestController
public class StockController {

    @Resource
    private StockService stockService;

    @GetMapping("/deduct")
    public String deduct() {

        this.stockService.deductUsingRedisson();
//        this.stockService.deductRedisReentrantDistributedLock();
//        this.stockService.deductRedisDistributedLock();
//        this.stockService.deductRedisOptimisticLock();
//        this.stockService.deductOptimisticLock();
//        this.stockService.deductPessimisticLock();
//        this.stockService.deductWithOneSql();
//        this.stockService.deductJVMLock();
//        this.stockService.deduct();

        return "验库存并锁库存成功！";
    }

}
