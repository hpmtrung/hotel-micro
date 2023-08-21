package com.hba.hbacore.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAround {
  boolean output() default true;
  boolean jsonOutput() default false;
  boolean input() default true;
  boolean jsonInput() default false;
}