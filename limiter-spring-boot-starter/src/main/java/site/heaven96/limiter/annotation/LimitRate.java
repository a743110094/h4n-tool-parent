package site.heaven96.limiter.annotation;


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
public @interface LimitRate {

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


}
