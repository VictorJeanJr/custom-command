package dev.victorjeanjr.customcommand.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String command();
    String permission() default "";
    String description() default "";
    boolean onlyPlayer() default false;

}
