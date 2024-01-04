package top.anets.im.module.feign.invoke;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ftm
 * @date 2023-11-10 15:36
 */
@RequestMapping("testFeign")
@RestController
public class TestFeignController {

    @Autowired
    private FeignInvokeService feignInvokeService;




    @RequestMapping("get")
    public String  get(){
        return feignInvokeService.test();
    }
}
