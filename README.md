# 1. 问题
1. 经过多次测试，除了程序连接池问题外都没有发现库存超卖或者遗留问题；
```$java
```
2. 程序在第一次启动后进行秒杀总是存在各种问题，但是进行第二次秒杀时问题就消失了。
```$xslt
该项调用的时候不应该放在成员变量为止进行对象引用初始化；而应该放在方法内部；
private static Jedis jedis = null;
```


# 测试结果
三次测试结果
1. 不借助任何辅助工具的情况下耗时
```$xslt
08:14:55.824 [main] INFO org.example.redisdemo.concurrent.ConcurrentSecKill - count:1000, 总耗时：2013
```

2. 添加事务及 watch 之后的耗时情况

```$xslt
08:16:29.627 [main] INFO org.example.redisdemo.concurrent.ConcurrentSecKill - count:1000, 总耗时：2182
```

3. 使用脚本的情况

```$xslt
08:17:30.496 [main] INFO org.example.redisdemo.concurrent.ConcurrentSecKill - count:999, 总耗时：2342
```

4. 使用 `synchronized` 关键字
```$xslt
08:19:19.575 [main] INFO org.example.redisdemo.concurrent.ConcurrentSecKill - count:1000, 总耗时：2353
```

# 分布式锁相关完善完毕

1000请求 100并发的测试情况；

**方式一**

```$xslt
06:29:18.265 [main] INFO org.example.redisdemo.distributedLock.LockTest - final count:1000, 总耗时：3843
```

**方式二**


```$xslt
06:33:01.604 [main] INFO org.example.redisdemo.distributedLock.LockTest - final count:1000, 总耗时：3004
```

方式而依赖特定版本：
```$xml
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
```
