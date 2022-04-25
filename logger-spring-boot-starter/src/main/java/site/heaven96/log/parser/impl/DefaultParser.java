package site.heaven96.log.parser.impl;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.log.constant.LogInfo;
import site.heaven96.log.parser.LogParser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * json序列化时,对HttpServletRequest处理时需要自定义JsonSerializer,这里不适合做任何处理
 * 所以此模块不适用于HttpServletRequest和HttpServletResponse或是JPA代理类这些对象
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultParser extends LogParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String DEFAULT = "default";

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
        return DEFAULT.equalsIgnoreCase(type);
    }

    @Override
    public String parse(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap();


        try {
            map.put("start", LocalDateTime.now());
            //出参
            Object outputArgs = null;
            outputArgs = joinPoint.proceed();
            //入参
            Object[] inputArgs = joinPoint.getArgs();
            map.put("inArgs",inputArgs);
            map.put("end", LocalDateTime.now());
            map.put("outArgs",outputArgs);
            map.put("class",joinPoint.getSignature().getDeclaringType());
            map.put("method",joinPoint.getSignature().toLongString());
            map.put("exp",false);
        }catch (Throwable throwable){
            map.put("exp",true);
            map.put("exps",throwable.toString());
            throw throwable;
        }finally {
            map.put("duration",System.currentTimeMillis()-startTime);
            return objectMapper.writeValueAsString(map);
        }

    }
}
