package site.heaven96.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.heaven96.log.aspect.H4nLogAspect;
import site.heaven96.log.constant.LogInfo;
import site.heaven96.log.handle.impl.DefaultLogHandle;
import site.heaven96.log.parser.impl.DefaultParser;
import site.heaven96.log.parser.impl.HttpRequestParser;
import site.heaven96.log.properties.H4nLogProperties;

@Configuration
//  @ConditionalOnProperty注解 控制@Configuration是否生效.
@ConditionalOnProperty(prefix = LogInfo.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
//使  @ConfigurationProperties
@EnableConfigurationProperties(H4nLogProperties.class)
public class H4nLogConfiguration {

    @Autowired
    private H4nLogProperties h4nLogProperties;

    @Bean
    public H4nLogAspect h4nLogAspect() {
        return new H4nLogAspect();
    }

    @Bean(value = "h4nDefaultLogHandle")
    public DefaultLogHandle h4nDefaultLogHandle() {
        return new DefaultLogHandle();
    }

    @Bean(value = "h4nDefaultParser")
    public DefaultParser h4nDefaultParser() {
        return new DefaultParser();
    }

    @Bean(value = "h4nHttpRequestParser")
    public HttpRequestParser h4nHttpRequestParser() {
        return new HttpRequestParser();
    }


}
