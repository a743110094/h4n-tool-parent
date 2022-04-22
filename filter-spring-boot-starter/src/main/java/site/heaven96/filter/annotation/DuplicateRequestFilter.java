package site.heaven96.filter.annotation;

import site.heaven96.filter.enums.Scope;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 重复请求过滤器
 *
 * @author heaven96
 * @date 2022/04/21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DuplicateRequestFilter {

    /**
     * 缓存持续时长
     *
     * @return long
     */
    long ttl() default 30L;

    /**
     * 时间单位
     *
     * @return {@code TimeUnit}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 范围
     *
     * @return {@code Scope}
     */
    Scope scope() default Scope.REQUEST_BODY;

}
