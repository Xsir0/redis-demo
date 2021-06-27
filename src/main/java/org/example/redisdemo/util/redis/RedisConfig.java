package org.example.redisdemo.util.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.InitBinder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiesir
 */
@Configuration
public class RedisConfig {


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private String timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private String maxWaitMillis;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;

    @Bean(name = "jedisPool")
    public  JedisPool redisPoolFactory() {
        // JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // jedisPoolConfig.setMaxIdle(maxIdle);
        // jedisPoolConfig.setMaxTotal(maxTotal);

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(maxIdle);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setMaxWaitMillis(Long.valueOf(maxWaitMillis.substring(0,maxWaitMillis.length()-2)));
        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, host, port,Integer.valueOf(timeout.substring(0,timeout.length()-2)));
        return jedisPool;
    }


}