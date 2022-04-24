package site.heaven96.log.parser;

import org.aspectj.lang.ProceedingJoinPoint;

public abstract class LogParser {

    public abstract boolean condition(String type);

    public abstract String parse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

}
