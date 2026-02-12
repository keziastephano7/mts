package com.moneytransfer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect for logging method calls and execution time
 *
 * This automatically logs:
 * - Method name
 * - Method arguments
 * - Execution time
 * - Return value or exception
 */
@Aspect  // Marks this as an AOP aspect
@Component  // Spring component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut: Define which methods to intercept
     * This intercepts all methods in service and controller packages
     */
    @Pointcut("execution(* com.moneytransfer.service..*(..)) || " +
            "execution(* com.moneytransfer.controller..*(..))")
    public void applicationMethods() {
        // This method is just a placeholder for the pointcut
    }

    /**
     * Around advice: Runs before and after the method
     *
     * @param joinPoint The intercepted method
     * @return The method's return value
     */
    @Around("applicationMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method information
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Log method entry
        log.debug("→ Entering {}.{}() with arguments: {}",
                className, methodName, Arrays.toString(args));

        // Record start time
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            // Execute the actual method
            result = joinPoint.proceed();

            // Calculate execution time
            long executionTime = System.currentTimeMillis() - startTime;

            // Log method exit
            log.debug("← Exiting {}.{}() with result: {} [Execution time: {}ms]",
                    className, methodName, result, executionTime);

            return result;

        } catch (Exception e) {
            // Calculate execution time even for exceptions
            long executionTime = System.currentTimeMillis() - startTime;

            // Log exception
            log.error("✗ Exception in {}.{}() after {}ms: {}",
                    className, methodName, executionTime, e.getMessage());

            // Re-throw the exception
            throw e;
        }
    }
}