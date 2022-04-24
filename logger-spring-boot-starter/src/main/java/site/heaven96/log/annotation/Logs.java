package site.heaven96.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logs {
    String value() default "";

    String handle() default ""; //可灵活指定使用那一个日记记录器进行处理，须得是springEL表达

    String type() default "default";//可灵活指定使用哪一个日志规则进行构建日志
}
