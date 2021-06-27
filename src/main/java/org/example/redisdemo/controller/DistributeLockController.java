package org.example.redisdemo.controller;

import org.example.redisdemo.distributedLock.LockTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DistributeLockController
 * @Description 分布式锁测试
 * @Author xsir
 * @Date 2021/6/27 下午8:21
 * @Version V1.0
 */
@RestController
@RequestMapping("distributed/lock")
public class DistributeLockController {


    @GetMapping
    public void test() throws InterruptedException {
        LockTest.main(null);
    }


}
