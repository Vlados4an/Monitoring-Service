package ru.erma.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that need to be audited.
 * The audit action is specified by the 'action' attribute.
 * The annotation is retained at runtime and can be applied to methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audit {
    /**
     * The action that will be audited. Defaults to an empty string if not specified.
     *
     * @return the audit action
     */
    String action() default "";

    /**
     * The username associated with the action. Defaults to an empty string if not specified.
     *
     * @return the username
     */
    String username() default "";

}
