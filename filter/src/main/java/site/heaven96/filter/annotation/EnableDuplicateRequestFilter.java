package site.heaven96.filter.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import site.heaven96.filter.enums.CacheStrategy;
import site.heaven96.filter.starter.support.DupReqConfigureSelector;

import java.lang.annotation.*;

import static org.springframework.context.annotation.AdviceMode.PROXY;
import static site.heaven96.filter.enums.CacheStrategy.LOCAL;

/**
 * 启用重复请求筛选
 *
 * @author lgw3488
 * @date 2022/04/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DupReqConfigureSelector.class})
public @interface EnableDuplicateRequestFilter {
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
