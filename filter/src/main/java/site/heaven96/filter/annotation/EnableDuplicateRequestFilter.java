package site.heaven96.filter.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import site.heaven96.filter.starter.support.DupReqConfigureSelector;

import java.lang.annotation.*;

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
    AdviceMode mode() default AdviceMode.PROXY;
}
