package top.anets.oauth2.sentinel;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableFeignDegrade {


    /**
     * 是否启用feign初始化熔断规则功能 默认使用
     *
     * @return
     */
    boolean enable() default true;

}