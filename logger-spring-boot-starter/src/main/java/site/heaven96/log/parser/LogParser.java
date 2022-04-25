package site.heaven96.log.parser;

import org.aspectj.lang.ProceedingJoinPoint;

public abstract class LogParser {
    /**
     * 判断是否需要使用此规则进行处理
     */
    public abstract boolean condition(String type);

    /**
     * 构建日志
     */
    public abstract String parse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

}
