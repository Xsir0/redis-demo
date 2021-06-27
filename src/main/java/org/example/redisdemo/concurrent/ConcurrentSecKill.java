package org.example.redisdemo.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.example.redisdemo.util.OkHttpUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author xsir
 * @date 2021年06月13日 上午8:04
 */
@Slf4j
public class ConcurrentSecKill {

    // 客户端请求数量
    public static int clientTotal = 1000;

    // 同时并发执行的线程数
    public static int threadTotal = 300;

    public static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        long startTime = System.currentTimeMillis();
        // 信号量
        final Semaphore semaphore = new Semaphore(threadTotal);
        // 计数器闭锁
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    // 判断当前线程是否被允许执行，达到一定并发之后，add 方法可能会被阻塞住 ，当 semaphore.acquire()返回许可之后，add 才会被执行
                    semaphore.acquire();
                    add();
                    // 执行完后释放许可
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception,", e);
                }
                // 每执行完一个线程，countDownLatch 的值就会 -1
                countDownLatch.countDown();
            });
        }

        // 该调用方法保证 所有的进程已经执行完， countDownLatch 的数值必须减为 0；
        countDownLatch.await();
        // 关闭线程池
        executorService.shutdown();
        // 在所有线程执行完之后，打印当前计数的值
        log.info("count:{}, 总耗时：{}",count,System.currentTimeMillis()-startTime);

    }

    public synchronized static void add(){
        count++;
        String result = OkHttpUtils.get("http://localhost:8080/secKill?pid=123", null, null);
        log.info("secKill result: {}",result);
    }

}
