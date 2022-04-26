package site.heaven96.log.parser.impl;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.heaven96.log.constant.LogInfo;
import site.heaven96.log.parser.LogParser;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class HttpRequestParser extends LogParser {

    public static final String SERVLET = "servlet";

    @Override
    public boolean condition(String type) {
       return SERVLET.equalsIgnoreCase(type);
    }

    @Override
    public String parse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String requestURI = request.getRequestURI();
        return requestURI;
    }
}
