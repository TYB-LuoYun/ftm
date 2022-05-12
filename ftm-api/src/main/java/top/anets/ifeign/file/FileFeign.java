package top.anets.ifeign.file;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.anets.utils.base.Result;

// value 指定是哪个微服务接口， // path 是在 Feign 调用时会加上此前缀，它与接口实现类的微服务中配置的
// context-path 值一致，如果微服务中 没有配置 context-path 下面就不需要写 path
@FeignClient(value="ftm-file", path = "/fileFeign")
public interface FileFeign{
    @GetMapping("/test")
    Result getFileTest();


}
