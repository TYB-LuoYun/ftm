package top.anets.config.feign;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.cloud.sentinel.feign.SentinelInvocationHandler;
import com.alibaba.cloud.sentinel.feign.SentinelTargeterAspect;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author ftm
 * @date 2023-08-25 16:09
 */
public class CustomSentinelFeign {
    private CustomSentinelFeign() {
    }

    public static CustomSentinelFeign.Builder builder() {
        return new CustomSentinelFeign.Builder();
    }

    public static final class Builder extends feign.Feign.Builder implements ApplicationContextAware {
        private Contract contract = new Contract.Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        public Builder() {
        }

        public feign.Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        public CustomSentinelFeign.Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @SneakyThrows
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    Object feignClientFactoryBean = SentinelTargeterAspect.getFeignClientFactoryBean();
//                    Object feignClientFactoryBean = CustomSentinelFeign.Builder.this.applicationContext.getBean("&" + target.type().getName());
                    Class fallback = (Class) CustomSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallback");
                    Class fallbackFactory = (Class) CustomSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallbackFactory");
                    String beanName = (String) CustomSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "contextId");
                    if (!StringUtils.hasText(beanName)) {
                        beanName = (String) CustomSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "name");
                    }


                    //获取指定的构造方法
                    Constructor<SentinelInvocationHandler> constructor = SentinelInvocationHandler.class.getDeclaredConstructor(Target.class,Map.class,FallbackFactory.class);
                    constructor.setAccessible(true);

                    String name = (String) getFieldValue(feignClientFactoryBean,"name");
                    Object fallbackInstance;
                    FallbackFactory fallbackFactoryInstance;

                    if (Void.TYPE != fallback) {
                        fallbackInstance = getFromContext(name,"fallback",fallback,target.type());
                        return constructor.newInstance(target, dispatch, new FallbackFactory.Default(fallbackInstance));
//                        Object fallbackInstance = this.getFromContext(beanName, "fallback", fallback, target.type());
//                        return new SentinelInvocationHandler(target, dispatch, new feign.hystrix.FallbackFactory.Default(fallbackInstance));
                    } else if (Void.TYPE != fallbackFactory) {
//                        FallbackFactory fallbackFactoryInstance = (FallbackFactory)this.getFromContext(beanName, "fallbackFactory", fallbackFactory, FallbackFactory.class);
//                        return new SentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
                        fallbackFactoryInstance = (FallbackFactory) getFromContext(name,"fallbackFactory", fallbackFactory,FallbackFactory.class);
                        return constructor.newInstance(target, dispatch, new FallbackFactory.Default(fallbackFactoryInstance));
                    } else {
//                        return new SentinelInvocationHandler(target, dispatch);
                        //默认的 fallbackFactory
                        GlobalFallbackFactory funFallbackFactory = new GlobalFallbackFactory(target);
                        return constructor.newInstance(target,dispatch, funFallbackFactory);
                    }
                }

                private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
                    Object fallbackInstance = CustomSentinelFeign.Builder.this.feignContext.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
                    } else if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", type, fallbackType, targetType, name));
                    } else {
                        return fallbackInstance;
                    }
                }
            });
            super.contract(new SentinelContractHolder(this.contract));
            return super.build();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            field.setAccessible(true);

            try {
                return field.get(instance);
            } catch (IllegalAccessException var5) {
                return null;
            }
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            this.feignContext = (FeignContext)this.applicationContext.getBean(FeignContext.class);
        }
    }
}
