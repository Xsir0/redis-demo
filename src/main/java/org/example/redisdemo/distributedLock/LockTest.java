package org.example.redisdemo.distributedLock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName LockTest
 * @Description 测试锁
 * @Author xsir
 * @Date 2021/6/27 下午5:18
 * @Version V1.0
 */
@Slf4j
public class LockTest {

    // 总请求数
    private static int totalThread = 1000;
    // 并发请求数
    private static int concurrentThread = 100;

    private static int count=0;

    public static void main(String[] args) throws InterruptedException {
        count =0;

        final CountDownLatch countDownLatch = new CountDownLatch(totalThread);

        final Semaphore semaphore = new Semaphore(concurrentThread);

        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < totalThread; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        count();
                        semaphore.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        countDownLatch.await();
        executor.shutdown();
        log.info("final count: {},countDownLatch: {}",count,countDownLatch.getCount());
    }
    public static void count(){
        String key = "testKey";
        String value = RandomStringUtils.randomAlphabetic(10);
        LockUtil.lock(key,value);
        try {
            count++;
        }finally {
            LockUtil.unlock(key,value);
        }
        log.info("count: {}",count);
    }
}
