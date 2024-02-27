package ru.erma.customenableannotation.aop.annotation;

import org.springframework.context.annotation.Import;
import ru.erma.customenableannotation.configuration.EnableCustomLoggerAutoConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableCustomLoggerAutoConfiguration.class)
public @interface EnableCustomLogger {
}
