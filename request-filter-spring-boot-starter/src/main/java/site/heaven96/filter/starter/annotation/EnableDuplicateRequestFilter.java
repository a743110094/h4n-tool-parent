package site.heaven96.filter.starter.annotation;


import filter.enums.CacheStrategy;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import site.heaven96.filter.starter.support.DupReqConfigureSelector;

import java.lang.annotation.*;

import static filter.enums.CacheStrategy.LOCAL;
import static org.springframework.context.annotation.AdviceMode.PROXY;

/**
 * 启用重复请求筛选
 *
 * @author heaven96
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
