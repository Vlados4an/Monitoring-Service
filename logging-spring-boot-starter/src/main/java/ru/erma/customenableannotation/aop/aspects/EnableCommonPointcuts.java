package ru.erma.customenableannotation.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class EnableCommonPointcuts {
    @Pointcut("@annotation(ru.erma.customenableannotation.aop.annotation.ActionLogger)")
    public void isCustomLogging() {
    }

}
