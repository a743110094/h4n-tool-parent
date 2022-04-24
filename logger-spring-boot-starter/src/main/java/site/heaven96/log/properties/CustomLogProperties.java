package site.heaven96.log.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import site.heaven96.log.constant.LogInfo;

@ConfigurationProperties(prefix = LogInfo.PREFIX)
public class CustomLogProperties {

    private Boolean enable;

    private String defaultHandle;


    public String getDefaultHandle() {
        return defaultHandle;
    }

    public void setDefaultHandle(String defaultHandle) {
        this.defaultHandle = defaultHandle;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
