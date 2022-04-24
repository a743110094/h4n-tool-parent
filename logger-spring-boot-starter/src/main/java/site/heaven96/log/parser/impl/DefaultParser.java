package site.heaven96.log.parser.impl;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import site.heaven96.log.parser.LogParser;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultParser extends LogParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
    }

    @Override
    public boolean condition(String type) {
        if (type.equals("default")) {
            return true;
        }
        return false;
    }

    @Override
    public String parse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] signatureArgs = joinPoint.getArgs();
        String s = objectMapper.writeValueAsString(signatureArgs);
        return s;
    }
}
