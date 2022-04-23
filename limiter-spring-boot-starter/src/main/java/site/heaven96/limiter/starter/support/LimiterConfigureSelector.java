package site.heaven96.limiter.starter.support;


import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.lang.Nullable;
import site.heaven96.limiter.annotation.EnableLimiter;
import site.heaven96.limiter.starter.configuration.LimiterAutoConfiguration;

/**
 * 重复请求配置选择器
 *
 * @author heaven96
 * @date 2022/04/21
 */
public class LimiterConfigureSelector extends AdviceModeImportSelector<EnableLimiter> {

    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "site.heaven96.limiter.starter.configuration.LimiterAutoConfiguration";

    public LimiterConfigureSelector() {
    }

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{LimiterAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }
}
