package site.heaven96.log.parser.impl;


import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.heaven96.log.parser.LogParser;

import javax.servlet.http.HttpServletRequest;


@Order(Ordered.LOWEST_PRECEDENCE)
public class HttpRequestParser extends LogParser {
    @Override
    public boolean condition(String type) {
        if (type.equals("servlet")) {
            return true;
        }
        return false;
    }

    @Override
    public String parse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String requestURI = request.getRequestURI();
        return requestURI;
    }
}
