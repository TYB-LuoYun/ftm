package top.anets.common.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ftm
 * @date 2023-12-27 13:59
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Response {
}

