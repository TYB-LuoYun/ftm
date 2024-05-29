package top.anets.cloud.base.feign.config;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import top.anets.boot.exception.ServiceException;

import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Objects;

/**
 * @author ftm
 * @date 2023-08-25 15:46
 * 全局的fallback处理器
 */
@Slf4j
@AllArgsConstructor
public  class GlobalFeignFallback<T> implements MethodInterceptor {
    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        /**
         * 进入降级方法了
         */
        log.error("GlobalFeionFallback: [{}.{}] serviceld:[{}]message:[{}] ", targetType.getName(), method.getName(),targetName,errorMessage);
// 非 ServiceException，直接返回if (!(cause instanceof FeianException))
//此处只是示例，具体可以返回带有业务错误数据的对象return Result.failure(BusinessExceptionEnumSYSTEM INNER ERROR,errorMessage);
//ServiceException exception = (ServiceException) cause:
//此处只是示例，具体可以返回带有业务错误数据的对象
        cause.printStackTrace();
        if(cause instanceof ServiceException){
            throw new ServiceException(errorMessage);
        }else if(cause instanceof DegradeException){
            throw new ServiceException("已熔断或降级，请稍后再试!");
        }else if(cause.getCause()!=null ){
            if(cause.getCause() instanceof com.netflix.client.ClientException){
                throw new ServiceException(targetName+"服务离线");
            }else if(cause.getCause() instanceof SocketTimeoutException){
                throw new ServiceException(targetName+"服务处理超时");
            }else if(cause.getCause() instanceof ConnectException){
                throw new ServiceException(targetName+"服务连接异常");
            }else{
                throw new ServiceException(errorMessage);
            }
        }else{ 
            throw new ServiceException(errorMessage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GlobalFeignFallback<?> that = (GlobalFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(targetType);
    }
}