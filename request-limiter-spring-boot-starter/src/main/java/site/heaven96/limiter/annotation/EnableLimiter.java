package site.heaven96.limiter.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import site.heaven96.limiter.enums.CacheStrategy;
import site.heaven96.limiter.starter.support.LimiterConfigureSelector;

import java.lang.annotation.*;

import static org.springframework.context.annotation.AdviceMode.PROXY;
import static site.heaven96.limiter.enums.CacheStrategy.LOCAL;

/**
 * 启用重复请求筛选
 *
 * @author heaven96
 * @date 2022/04/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LimiterConfigureSelector.class})
public @interface EnableLimiter {

    /**
     * 策略
     *
     * @return {@code CacheStrategy}
     */
    CacheStrategy strategy() default LOCAL;


    /**
     * 模式
     *
     * @return {@code AdviceMode}
     */
    AdviceMode mode() default PROXY;
}
