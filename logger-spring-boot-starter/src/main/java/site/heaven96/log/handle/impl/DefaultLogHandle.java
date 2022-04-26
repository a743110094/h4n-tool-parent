package site.heaven96.log.handle.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import site.heaven96.log.handle.LogHandle;

@Order(1)
public class DefaultLogHandle extends LogHandle {

    private static final Logger log = LoggerFactory.getLogger(DefaultLogHandle.class);

    @Override
    public void handle(String value) {
        log.info("日志:" + value);
    }
}
