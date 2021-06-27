package org.example.redisdemo.controller;

import org.example.redisdemo.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName SecKillController
 * @Description 秒杀接口
 * @Author xsir
 * @Date 2021/6/26 上午11:33
 * @Version V1.0
 */
@RestController
@RequestMapping("/secKill")
public class SecKillController {

    // @Autowired
    // private SecKillService secKillService;

    @Resource(name = "safeSecKill")
    private SecKillService secKillService;

    // @Resource(name = "scriptSecKill")
    // private SecKillService secKillService;


    @GetMapping
    public String secKill(@RequestParam(required = true) String pid) throws IOException {
        String uid = UUID.randomUUID().toString().replace("-", "");
        return secKillService.secKill(uid,pid);
    }

}
