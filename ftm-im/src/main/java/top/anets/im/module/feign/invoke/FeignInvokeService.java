package top.anets.im.module.feign.invoke;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ftm
 * @date 2023-11-14 11:12
 */
@FeignClient(name="im")
public interface FeignInvokeService {
    @RequestMapping("/test")
    String test();

    @RequestMapping("/get")
    public String get();
}
