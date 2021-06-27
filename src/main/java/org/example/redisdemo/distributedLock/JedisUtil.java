package org.example.redisdemo.distributedLock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.example.redisdemo.util.SpringContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.Objects;

/**
 * @ClassName JedisUtil
 * @Description Jedis 锁工具
 * @Author xsir
 * @Date 2021/6/27 上午11:24
 * @Version V1.0
 */
@Slf4j
public class JedisUtil {


    // private static Jedis jedis = null; 这种写法连接会出问题。
    private static JedisPool jedisPool;

    static {
        // web
        // jedisPool = (JedisPool) SpringContextUtil.getBean("jedisPool");

        // java
        jedisPool = new JedisPool();
    }

    /**
     * @desc 归还链接
     * @author xsir
     * @date 2021/6/27 上午11:46
     * @param jedis
     */
    private static void release(Jedis jedis){
        if (jedis != null){
            jedis.close();
        }
    }

    /**
     * @desc 获取锁
     * @author xsir
     * @date 2021/6/27 上午11:46
     * @param key
     * @param value
     * @param expiresMillis
     * @return boolean
     */
    public static  boolean lock(String key,String value,long expiresMillis){
        Jedis   jedis = jedisPool.getResource();
       try {


           // 方式一
           Long setnx = jedis.setnx(key, value);
           if (1l == setnx){
               jedis.setex(key, expiresMillis, value);
               return true;
           }

           // 方式二 性能优于 方式一
           // String result = jedis.set(key, value, "NX", "PX", expiresMillis);
           // if (result != null && result.equalsIgnoreCase("OK")) {
           //     return true;
           // }
       }finally {
        release(jedis);
       }
        return false;
    }

    /**
     * @desc 解锁
     * @author xsir
     * @date 2021/6/27 上午11:45
     * @param key
     * @param value
     * @return boolean
     */
    public static boolean unlock(String key,String value){
        Jedis  jedis = jedisPool.getResource();
        try {

            // 方式一
            jedis.watch(key);
            String s = jedis.get(key);
            if (Objects.equals(s,value)){
                jedis.del(key);
                return true;
            }

            // 方式二
            // String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            // Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            // if (Objects.equals(1l,result)){
            //             jedis.del(key);
            //             return true;
            // }


            log.error("source key value[{}] not equals ",value);
            return false;
        }finally {
            release(jedis);
        }
    }
}