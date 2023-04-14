package org.xinhua.example.spring.aspect.logger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.aspect.logger.annotation.LogParam;
import org.xinhua.example.spring.aspect.logger.annotation.LogResult;
import org.xinhua.example.spring.aspect.logger.annotation.LogTime;

@RestController
public class TestController {

    @LogParam
    @LogResult
    @LogTime
    @GetMapping("/test")
    public Object test(String k1, String k2) {
        return k1 + "==" + k2;
    }

}
