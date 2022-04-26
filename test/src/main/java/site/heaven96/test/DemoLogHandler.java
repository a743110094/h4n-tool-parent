package site.heaven96.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import site.heaven96.log.handle.LogHandle;

@Component
public class DemoLogHandler extends LogHandle {

    private static final Logger log = LoggerFactory.getLogger(DemoLogHandler.class);
    @Override
    public void handle(String value) {
        log.info("自定义日志处理器:{}",value);
    }
}
