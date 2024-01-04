package top.anets.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.common.base.utils.Result;

/**
 * @author ftm
 * @date 2023-11-01 15:23
 */
@RequestMapping("test")
@RestController
public class TestController {
    @RequestMapping("get")
    public Result get(){
        return Result.success();
    }
}
