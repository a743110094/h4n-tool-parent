package site.heaven96.limiter.starter.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import site.heaven96.limiter.annotation.EnableLimiter;
import site.heaven96.limiter.common.aspect.LimiterAspect;

import java.util.logging.Logger;


/**
 * 速率限制器自动配置
 *
 * @author heaven96
 * @date 2022/04/22
 */
@Configuration
public class LimiterAutoConfiguration implements ImportAware {

    private Logger log = Logger.getLogger("LimiterAutoConfiguration");

    private AnnotationAttributes annotation;


    public LimiterAutoConfiguration() {
    }

    @Bean
    @Role(0)
    public LimiterAspect limiterAspect() {
        return new LimiterAspect();
    }

    /**
     * Set the site.heaven96.log.annotation metadata of the importing @{@code Configuration} class.
     *
     * @param importMetadata
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotation = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableLimiter.class.getName(), false));
        if (this.annotation == null) {
            log.info("@EnableLimiter is not present on importing class");
        }

    }
}