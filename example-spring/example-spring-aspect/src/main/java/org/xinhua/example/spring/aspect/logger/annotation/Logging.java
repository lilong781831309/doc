package org.xinhua.example.spring.aspect.logger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    LogParam[] logParam() default {};

    LogResult[] logResult() default {};

    LogError[] logError() default {};

    LogTime[] logTime() default {};

}