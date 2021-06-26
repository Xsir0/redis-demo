package org.example.redisdemo.util.redis;

import java.util.Set;

/**
 * @author xiesir
 * redis 通用服务
 */
public interface RedisBaseService {


    String setEx(String key, int seconds, String value, RedisDB dbIndex);

    String set(String key, String value, RedisDB dbIndex);

    String set(String key, String value);

    String get(String key, RedisDB dbIndex);

    String get(String key);

    Long del(String key);

    Long del(String key, RedisDB dbIndex);

    Boolean hasKey(String key);

    Boolean hasKey(String key, RedisDB dbIndex);

    Set<String> keys(String key);

    Set<String> keys(String key, RedisDB dbIndex);

    Long getExpire(String key, RedisDB dbIndex);

}
