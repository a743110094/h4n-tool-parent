package site.heaven96.log.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import site.heaven96.log.constant.LogInfo;

@ConfigurationProperties(prefix = LogInfo.PREFIX)
public class H4nLogProperties {

    /**
     * 启用
     */
    private Boolean enable;

    /**
     * 默认处理
     */
    private String defaultLogHandler;

    public String getDefaultLogHandler() {
        return defaultLogHandler;
    }

    public void setDefaultLogHandler(String defaultLogHandler) {
        this.defaultLogHandler = defaultLogHandler;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
