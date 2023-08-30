package top.anets.config.feign;

import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author ftm
 * @date 2023-08-25 16:03
 * 全局的FallbackFactory
 */
@AllArgsConstructor
public class GlobalFallbackFactory<T> implements FallbackFactory<T> {

    private final Target<T> target;

    @SuppressWarnings("unchecked")
    @Override
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new GlobalFeignFallback<>(targetType,targetName,cause));
        return (T) enhancer.create();
    }
}
