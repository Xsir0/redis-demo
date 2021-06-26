package org.example.redisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StartUpRunner
 * @Description 启动后加载配置
 * @Author xsir
 * @Date 2021/6/26 下午8:23
 * @Version V1.0
 */
@Slf4j
@Component
public class StartUpRunner implements CommandLineRunner {

    @Autowired
    private JedisPool jedisPool;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("=====================Redis Pool 预热开始=================");
        // List<Jedis> minIdleJedisList = new ArrayList<Jedis>(maxIdle);
        //
        // for (int i = 0; i < maxIdle; i++) {
        //     Jedis jedis = null;
        //     try {
        //         jedis = jedisPool.getResource();
        //         minIdleJedisList.add(jedis);
        //         jedis.ping();
        //     } catch (Exception e) {
        //         log.error(e.getMessage(), e);
        //     } finally {
        //     }
        // }
        //
        // for (int i = 0; i < maxIdle; i++) {
        //     Jedis jedis = null;
        //     try {
        //         jedis = minIdleJedisList.get(i);
        //         jedis.close();
        //     } catch (Exception e) {
        //         log.error(e.getMessage(), e);
        //     } finally {
        //
        //     }
        // }

        System.out.println("=====================Redis Pool 预热结束=================");

    }
}
