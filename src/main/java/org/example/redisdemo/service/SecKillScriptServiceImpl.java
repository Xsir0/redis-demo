package org.example.redisdemo.service;

import com.power.common.util.FileUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.redisdemo.util.redis.RedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @ClassName SecKillServiceImpl
 * @Description 加载LUA 脚本解决 库存遗留问题
 * @Author xsir
 * @Date 2021/6/26 上午11:53
 * @Version V1.0
 */
@Slf4j
@Service("scriptSecKill")
public class SecKillScriptServiceImpl implements SecKillService {


    @Autowired
    private JedisPool jedisPool;

    private static Jedis jedis;

    @Override
    public String secKill(String uId,String pid) throws IOException {

        try {
            jedis = jedisPool.getResource();
            jedis.select(RedisDB.ONE.getOrdinal());

            ClassPathResource classPathResource = new ClassPathResource("seckillScript.lua");
            String scriptContent = jedis.scriptLoad(FileUtil.getFileContent(new FileInputStream(classPathResource.getFile())));

            Object result = jedis.evalsha(scriptContent, 2, uId, pid);

            String status = String.valueOf(result);
            if ("0".equals(status)){
                return "秒杀已结束>>>";
            }else if ("1".equals(status)){
                return "秒杀成功>>>";
            }else if ("2".equals(status)){
                return "请勿重复秒杀>>>";
            }else {
                return "UNKNOWN ERROR: "+status;
            }
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }
}
