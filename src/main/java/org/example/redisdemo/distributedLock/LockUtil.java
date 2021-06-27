package org.example.redisdemo.distributedLock;

import com.sun.org.apache.bcel.internal.generic.FADD;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName LockUtil
 * @Description Redis 锁工具类
 * @Author xsir
 * @Date 2021/6/27 上午11:16
 * @Version V1.0
 */
@Slf4j
public class LockUtil {

    // 锁过期时间
    private static long DEFAULT_LOCK_TIME_MILLIS = 20000L;
    // 重试次数
    private static int DEFAULT_RETRY_TIMES  = 3;
    // 重试超时时间
    private static long DEFAULT_TRY_LOCK_TIMEOUT_MILLIS = 100L;


    /**
     * @desc 获取锁
     * @author xsir
     * @date 2021/6/27 上午11:18
     * @param key
     * @param value
     * @return boolean
     */
    public static boolean lock(String key,String value){
        return lock(key,value,DEFAULT_LOCK_TIME_MILLIS);
    }

    // public static boolean lock(String key,String value,long lockTimeoutMillis){
    //     return lock(key,value,false,0,lockTimeoutMillis,0);
    // }

    public static boolean tryLock(String key,String value){
        return tryLock(key,value,DEFAULT_RETRY_TIMES);
    }

    public static boolean tryLock(String key,String value,long lockTimeMillis){
        return tryLock(key,value,DEFAULT_RETRY_TIMES,lockTimeMillis);
    }

    public static boolean tryLock(String key,String value,int retryTimes){
        return tryLock(key,value,retryTimes,DEFAULT_TRY_LOCK_TIMEOUT_MILLIS);
    }

    public static boolean tryLock(String key,String value,int retryTimes,long lockTimeMillis){
        return tryLock(key,value,retryTimes,lockTimeMillis,DEFAULT_TRY_LOCK_TIMEOUT_MILLIS);
    }

    public static boolean tryLock(String key,String value,int retryTimes,long lockTimeMillis,long retryTimeoutMillis){
        return lock(key,value,true,retryTimes,lockTimeMillis,retryTimeoutMillis);
    }

    private static boolean lock(String key,String value,long lockTimeMillis){
        log.info("开始获取锁 >>>  key: {}, value: {}",key,value);
        boolean lockStatus = JedisUtil.lock(key, value, lockTimeMillis);
        if (lockStatus){
            log.info("成功 >>> key: {}, value: {}",key,value);
            return true;
        }
        sleep(DEFAULT_TRY_LOCK_TIMEOUT_MILLIS);
        return lock(key,value,lockTimeMillis);

    }




    /**
     * @desc 基类 lockTime:20,reTry:true,curTryTime:0,needTimeOut:false,timeOutMillis:0
     * @author xsir
     * @date 2021/6/27 上午11:19
     * @param key
     * @param value
     * @param retry 是否进行重试
     * @param retryTimeMillis 重试时间间隔
     * @return int
     */
    private static boolean lock(String key, String value, boolean retry,int retryTimes,long lockTimeMillis, long retryTimeMillis){

        log.info("开始获取锁>>>  key: {}, value: {},lockTime: {}, retry: {}",key,value,lockTimeMillis,retry);
        // 获取锁
        boolean lockStatus = JedisUtil.lock(key, value, lockTimeMillis);
        if (lockStatus){
            log.info("{} 获取锁成功>>>  key: {}, value: {},lockTime: {}, retry: {}",key,value,lockTimeMillis,retry);
            return true;
        }

        //是否进行重试
        if (!retry) {
            log.info("{} 获取锁失败>>>  key: {}, value: {},lockTime: {}, retry: false",key,value,lockTimeMillis);
            return false;
        }

        // 获取sleep时间
        long sleepMillis = getSleepMillis(false, retryTimeMillis);

        // sleep后重新获取锁
        sleep(sleepMillis);

        return lock(key, value, retry, retryTimes, lockTimeMillis, retryTimeMillis);
    }

    public static void sleep(long sleepTimeMillis){
        try{
            Thread.sleep(sleepTimeMillis);
        }catch (InterruptedException e){
            log.error("重试发生故障：{}",e.getMessage());
        }
    }

    private static long getSleepMillis(boolean needTimeOut, long timeOutMillis) {
        long sleepMillis = DEFAULT_TRY_LOCK_TIMEOUT_MILLIS;
        if (needTimeOut) {
            timeOutMillis = timeOutMillis - DEFAULT_TRY_LOCK_TIMEOUT_MILLIS;
            if (timeOutMillis < DEFAULT_TRY_LOCK_TIMEOUT_MILLIS) {
                sleepMillis = timeOutMillis;
            }
        }
        return sleepMillis;
    }



    /**
     * @desc 解锁
     * @author xsir
     * @date 2021/6/27 上午11:20
     * @param key
     * @param value
     * @return boolean
     */
    public static boolean unlock(String key,String value){
        boolean unlock = JedisUtil.unlock(key, value);
        if (unlock){
            log.info("解锁成功 key: {}, value: {}",key,value);
        }else {
            log.info("解锁失败 key: {}, value: {}",key,value);
        }
        return unlock;
    }



}
