//package top.anets.config.feign;
//
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import top.anets.utils.exception.ServiceException;
//
///**
// * @author ftm
// * @date 2023-08-25 10:10
// */
///**1 业务发生异常
// * 2 feign捕获到错误
// * 3 走降级方法
// * FeignClient 设置fallbackFactory 返回统一格式异常  与fallback不能共用
// * ServiceException 是自定义的异常类，ErrorCode.SERVICE_ERROR是自定义错误码，请自行创建。
// */
//@Slf4j
//@Component
//public class GeneralFallbackFactory implements FallbackFactory {
//    @Override
//    public Object create(Throwable throwable) {
//        log.info("通用降级处理");
//        throw new ServiceException(throwable.getMessage());
//    }
//}
