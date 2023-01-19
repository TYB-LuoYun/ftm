package top.anets.search.top.anets.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping("get")
    public void get(){
        log.info("我们的发发发");
        log.error("dddddddssIij诶简单介绍看看");
    }
}
