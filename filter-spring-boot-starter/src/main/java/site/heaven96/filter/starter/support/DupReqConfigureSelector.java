package site.heaven96.filter.starter.support;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.lang.Nullable;
import site.heaven96.filter.annotation.EnableDuplicateRequestFilter;
import site.heaven96.filter.starter.configuration.DupReqFilterAutoConfiguration;


/**
 * 重复请求配置选择器
 *
 * @author heaven96
 * @date 2022/04/21
 */
public class DupReqConfigureSelector extends AdviceModeImportSelector<EnableDuplicateRequestFilter> {

    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "site.heaven96.filter.site.heaven96.limiter.starter.configuration.DupReqFilterAutoConfiguration";

    public DupReqConfigureSelector() {
    }

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{DupReqFilterAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }
}
