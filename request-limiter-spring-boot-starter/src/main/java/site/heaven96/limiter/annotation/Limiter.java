package site.heaven96.limiter.annotation;


import site.heaven96.limiter.enums.IdentifierType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 速率限制器
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
     * 单位时间最大调用次数
     * ceil
     *
     * @return long
     */
    long ceiling() default 20L;

    /**
     * 时间周期
     *
     * @return long
     */
    long period() default 5L;

    /**
     * 时间单位
     *
     * @return {@code TimeUnit}
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 默认出错消息
     *
     * @return long
     */
    String message() default DEFAULT_ERR_MSG;

    /**
     * 是否依据用户独立控制限速
     *
     * @return boolean
     */
    IdentifierType independence() default IdentifierType.NONE;

    /**
     * 放置用户标志信息的键名(不论是在Header还是Parameter或是Session)
     *
     * @return {@link String}
     */
    String key() default "";


}
