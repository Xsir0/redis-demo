package org.example.redisdemo.service;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.redisdemo.util.redis.RedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName SecKillServiceImplSafe
 * @Description  安全版本
 * @Author xsir
 * @Date 2021/6/26 下午6:20
 * @Version V1.0
 */
@Slf4j
@Service("safeSecKill")
public class SecKillServiceImplSafe implements SecKillService {

    @Autowired
    private JedisPool jedisPool;

    private static Jedis jedis;


    @Override
    public String secKill(String uId, String pid) {
        jedis = jedisPool.getResource();
        jedis.select(RedisDB.ONE.getOrdinal());

        // log.info("uid: {}, pid: {}",uId,pid);

        try{
            // 库存key
            String kcKey = StringUtil.join(":", Arrays.asList("sk",pid,"qt")).toString();
            //构建秒杀用户 key
            String userKey = StringUtil.join(":",Arrays.asList("sk",pid,"user")).toString();


            // 监视库存
            jedis.watch(kcKey);

            // 获取库存
            String kcCount = jedis.get(kcKey);
            if (null == kcCount){
                log.info("秒杀未开始>>> ");
                return "秒杀未开始>>>";
            }
            // 查看用户是否已经秒杀
            if (jedis.sismember(userKey,uId)){
                log.info("请勿重复秒杀>>> kcKey: {}, kcCount: {}",kcKey,kcCount);
                return "请勿重复秒杀>>>";
            }

            // 进入秒杀
            // 查看库存是否充足
            if (Integer.parseInt(kcCount)<=0){
                log.info("秒杀已经结束>>> kcKey: {}, kcCount: {}",kcKey,kcCount);
                return "秒杀已经结束>>>";
            }

            // 开启事务
            Transaction multi = jedis.multi();
            // 命令放入队列

            // 减库存
            multi.decr(kcKey);
            // 构建用户秒杀成功key
            multi.sadd(userKey,uId);
            List<Object> exec = multi.exec();

            if (exec == null || exec.size() == 0){
                return "秒杀失败";
            }
            log.info("秒杀成功,当前库存剩余：{}",exec.get(0));
        }finally {
            if (jedis != null)
                jedis.close();
        }

        return new StringBuffer().append("用户："+uId +" 秒杀 "+pid+"成功").toString();
    }
}
