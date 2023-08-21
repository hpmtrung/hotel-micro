package com.hba.hbacore.aop;

import com.hba.hbacore.util.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfig {

  private static final String DEV_PROFILE = "dev";

  @Bean
  @Profile(DEV_PROFILE)
  public LoggingAspect loggingAspect(Environment env) {
    return new LoggingAspect(env);
  }

  @Aspect
  public static class LoggingAspect {

    private final Environment env;

    public LoggingAspect(Environment env) {
      this.env = env;
    }

    /** Pointcut that matches all repositories, services and Web REST endpoints. */
    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)"
            + " || within(@org.springframework.stereotype.Service *)"
            + " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
      // ServiceParty is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /** Pointcut that matches all Spring beans in the application's main packages. */
    @Pointcut(
        "within(vn.lotusviet.hotelmgmt.repository..*)"
            + " || within(vn.lotusviet.hotelmgmt.service..*)"
            + " || within(vn.lotusviet.hotelmgmt.scheduled..*)"
            + " || within(vn.lotusviet.hotelmgmt.security..*)"
            + " || within(vn.lotusviet.hotelmgmt.web..*)")
    public void applicationPackagePointcut() {
      // ServiceParty is empty as this is just a Pointcut, the implementations are in the advices.
    }

    public <T extends Annotation> T getAnnotationOfJoinPoint(
        JoinPoint joinPoint, Class<T> annotationClass) {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      T result = method.getAnnotation(annotationClass);
      if (result != null) {
        return result;
      } else {
        return method.getDeclaringClass().getAnnotation(annotationClass);
      }
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice.
     * @param e exception.
     */
    @AfterThrowing(
        pointcut = "applicationPackagePointcut() && springBeanPointcut()",
        throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
      if (env.acceptsProfiles(Profiles.of(DEV_PROFILE))) {
        logger(joinPoint)
            .error(
                "Exception in {}() with cause = '{}' and exception = '{}'",
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e);
      } else {
        logger(joinPoint)
            .error(
                "Exception in {}() with cause = {}",
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL");
      }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
      Logger log = logger(joinPoint);

      final LogAround logAround = getAnnotationOfJoinPoint(joinPoint, LogAround.class);
      boolean logInput = false;
      boolean logOutput = false;
      boolean jsonInput = false;
      boolean jsonOuput = false;

      if (logAround != null && log.isDebugEnabled()) {
        logInput = logAround.input();
        logOutput = logAround.output();
        jsonInput = logAround.jsonInput();
        jsonOuput = logAround.jsonOutput();
      }

      final Object[] arguments = joinPoint.getArgs();

      final String argumentsAsString =
          jsonInput ? JacksonUtil.serializeJson(arguments) : Arrays.toString(arguments);
      final String signatureName = joinPoint.getSignature().getName();

      if (logInput) {
        log.debug("Enter: {}() with argument[s] = {}", signatureName, argumentsAsString);
      }
      try {
        final Object result = joinPoint.proceed();
        final String resultAsString =
            jsonOuput ? JacksonUtil.serializeJson(result) : String.valueOf(result);
        if (logOutput) {
          log.debug("Exit: {}() with result = {}", signatureName, resultAsString);
        }
        return result;
      } catch (IllegalArgumentException e) {
        log.error("Illegal argument: {} in {}()", argumentsAsString, signatureName);
        throw e;
      }
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
      return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }
  }
}