package org.example.redisdemo.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: exchange
 * @description: redis抽象服务
 * @author: Xsir
 * @create: 2020-06-11 13:42
 **/
@Slf4j
public abstract class AbstractRedisService implements RedisBaseService {

    //@Value("${spring.redis.database}")
    private static int database = 0;

    @Autowired
    private JedisPool jedisPool;


    private void returnResource(Jedis jedis) {
        jedis.close();
    }

    private Jedis getResource(Integer dbIndex) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        return jedis;
    }

    private Jedis getResource(){
        return getResource(database);
    }

    @Override
    public String setEx(String key, int seconds, String value, RedisDB dbIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public String set(String key, String value,RedisDB dbIndex){
        Jedis jedis = null;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            return jedis.set(key, value);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public String set(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.set(key, value);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public String get(String key,RedisDB dbIndex) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = null;
        Long status = null;
        try {
            jedis = getResource();
            status = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return status;
    }

    @Override
    public Long del(String key, RedisDB dbIndex) {
        Jedis jedis = null;
        Long status = null;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            status = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return status;
    }

    @Override
    public Boolean hasKey(String key) {
        Jedis jedis = null;
        boolean hasKey = false;
        try {
            jedis = getResource();
            hasKey = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return hasKey;
    }

    @Override
    public Boolean hasKey(String key, RedisDB dbIndex) {
        Jedis jedis = null;
        boolean hasKey = false;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            hasKey = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return hasKey;
    }


    @Override
    public Set<String> keys(String key) {
        Jedis jedis = null;
        Set<String> hasKey = new HashSet<>();
        try {
            jedis = getResource();
            hasKey = jedis.keys(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return hasKey;
    }

    @Override
    public Set<String> keys(String key, RedisDB dbIndex) {
        Jedis jedis = null;
        Set<String> hasKey = new HashSet<>();
        try {
            jedis = getResource(dbIndex.getOrdinal());
            hasKey = jedis.keys(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return hasKey;
    }

    @Override
    public Long getExpire(String key,RedisDB dbIndex) {
        Jedis jedis = null;
        Long expireTime = 0L;
        try {
            jedis = getResource(dbIndex.getOrdinal());
            expireTime = jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return expireTime;
    }
}
