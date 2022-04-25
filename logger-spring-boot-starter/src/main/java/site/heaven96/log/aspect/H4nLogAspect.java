package site.heaven96.log.aspect;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
import site.heaven96.log.annotation.Logs;
import site.heaven96.log.handle.LogHandle;
import site.heaven96.log.parser.LogParser;
import site.heaven96.log.properties.H4nLogProperties;

import java.util.List;

@Aspect
@Order(0)
@Slf4j
public class H4nLogAspect {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        //TODO
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
    }


    @Autowired
    private ApplicationContext factory;

    @Autowired
    private List<LogParser> logParsers;

    @Autowired
    private H4nLogProperties customLogProperties;
    /**
     * 配置切入点
     * 为了通用性这里没有采用args()处理,如果是不考虑通用性的话，建议使用args进行切面，这样可以避免使用反射操作，直接拿到方法中参数的值
     * @Pointcut("com.xyz.myapp.SystemArchitecture.dataAccessOperation() &&"args(account,..)")
     * private void accountDataAccessOperation(Account account) {}
     * https://www.programmersought.com/article/87257200177/
     */

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(logs)")
    public void logPointcut(Logs logs) {
    }

    @Around("logPointcut(logs)")
    public Object logAround(ProceedingJoinPoint joinPoint, Logs logs) throws Throwable {
        log.debug("testLog");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(factory));
        ExpressionParser parser = new SpelExpressionParser();

        String handle = StringUtils.hasText(logs.handle()) ? logs.handle() : customLogProperties.getDefaultLogHandler();

        if (!StringUtils.hasText(handle)) {
            log.error("could not found site.heaven96.log.handle");
        } else {
            LogHandle logHandle = (LogHandle) parser.parseExpression(handle).getValue(context);
            String value = null;
            for (LogParser logParser : logParsers) {
                if (logParser.condition(logs.type())) {
                    try {
                        value = logParser.parse(joinPoint);
                    } catch (Exception e) {
                        log.error("Something wrong during log parsing : \n {}", e.getCause());
                    }
                }
            }
            logHandle.handle(value);
        }
        return joinPoint.proceed();
    }

}
