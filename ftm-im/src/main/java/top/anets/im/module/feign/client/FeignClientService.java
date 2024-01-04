package top.anets.im.module.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.common.utils.exception.ServiceException;

/**
 * @author ftm
 * @date 2023-11-14 10:31
 * name属性会作为微服务的名称，用于服务发现
 */
@RestController
public class FeignClientService {
    @RequestMapping("/test")
    public String test(){
        if(true){
            throw new ServiceException("错误");
        }
        return "SUCCESS";
    }
}
