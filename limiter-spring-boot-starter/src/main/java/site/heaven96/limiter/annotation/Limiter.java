package site.heaven96.limiter.annotation;


import site.heaven96.limiter.enums.IdentifierType;

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
public @interface Limiter {

    /**
     * 默认出错消息
     */
    String DEFAULT_ERR_MSG = "接口繁忙,请稍后重试";

    /**
     * 最大调用次数
     *
     * @return long
     */
    long callLimit() default 20L;

    /**
     * 缓存持续时长
     *
     * @return long
     */
    long ttl() default 5L;

    /**
     * 时间单位
     *
     * @return {@code TimeUnit}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 默认出错消息
     *
     * @return long
     */
    String message() default DEFAULT_ERR_MSG;

    /**
     * 区分用户限制
     *
     * @return boolean
     */
    IdentifierType byUser() default IdentifierType.NONE;

    /**
     * 请求头放置用户标志信息的键名
     *
     * @return {@link String}
     */
    String headerKey() default "";

    /**
     * 参数放置用户标志信息的键名
     *
     * @return {@link String}
     */
    String paramKey() default "";


    /**
     * Session放置用户标志信息的键名
     *
     * @return {@link String}
     */
    String sessionKey() default "";

}
