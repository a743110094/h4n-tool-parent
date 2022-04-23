package site.heaven96.filter.starter.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import site.heaven96.filter.annotation.EnableDuplicateRequestFilter;
import site.heaven96.filter.aspect.DupReqFilterAspect;

import java.util.logging.Logger;


/**
 * 重复请求过滤器自动配置
 *
 * @author heaven96
 * @date 2022/04/22
 */
@Configuration
public class DupReqFilterAutoConfiguration implements ImportAware {

    private Logger log = java.util.logging.Logger.getLogger("DupReqFilterAutoConfiguration");

    private AnnotationAttributes dupReq;


    public DupReqFilterAutoConfiguration() {
    }

    @Bean
    @Role(0)
    public DupReqFilterAspect dupReqFilterAspect() {
        return new DupReqFilterAspect();
    }

    /**
     * Set the site.heaven96.filter.site.heaven96.limiter.annotation metadata of the importing @{@code Configuration} class.
     *
     * @param importMetadata
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.dupReq = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableDuplicateRequestFilter.class.getName(), false));
        if (this.dupReq == null) {
            log.info("@EnableDuplicateRequestFilter is not present on importing class");
        }

    }
}