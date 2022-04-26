package site.heaven96.log.handle.impl;

import org.springframework.core.annotation.Order;
import site.heaven96.log.handle.LogHandle;

@Order(2)
public class DefaultHttpRequestHandler extends LogHandle {

    @Override
    public void handle(String value) {

    }
}
