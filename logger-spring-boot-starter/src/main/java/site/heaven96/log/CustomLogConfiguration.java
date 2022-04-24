package site.heaven96.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.heaven96.log.aspect.CustomLogAspect;
import site.heaven96.log.constant.LogInfo;
import site.heaven96.log.handle.impl.DefaultLogHandle;
import site.heaven96.log.parser.impl.DefaultParser;
import site.heaven96.log.parser.impl.HttpRequestParser;
import site.heaven96.log.properties.CustomLogProperties;

@Configuration
@ConditionalOnProperty(prefix = LogInfo.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CustomLogProperties.class)
public class CustomLogConfiguration {

    @Autowired
    private CustomLogProperties customLogProperties;

    @Bean
    public CustomLogAspect customLogAspect() {
        return new CustomLogAspect();
    }

    @Bean(value = "defaultLogHandle")
    public DefaultLogHandle defaultLogHandle() {
        return new DefaultLogHandle();
    }

    @Bean
    public DefaultParser defaultParser() {
        return new DefaultParser();
    }

    @Bean
    public HttpRequestParser httpRequestParser() {
        return new HttpRequestParser();
    }


}
