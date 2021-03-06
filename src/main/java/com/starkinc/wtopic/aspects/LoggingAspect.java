package com.starkinc.wtopic.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author RajaSushanth
 *
 */
@Aspect
@Component
public class LoggingAspect {
	
	@Pointcut("execution(* com.starkinc.wtopic.*.*.*(..))")
	public void includedMethods(){}
	
	@Pointcut("! execution(* java.lang.reflect.Proxy+.*(..))"
		 + "&& ! execution(* com.starkinc.wtopic.config.JavaConfig.*(..))"
		 + "&& ! execution(* com.starkinc.wtopic.exception.CustomResponseErrorHandler.*(..))"
		 + "&& ! execution(* com.starkinc.wtopic.*.*.set*(..))"
		 + "&& ! execution(* com.starkinc.wtopic.restClient.TopicClient.*(..))")
	public void excludedMethods(){}
	
	@Around("includedMethods() && excludedMethods()")
	public Object logUser(ProceedingJoinPoint jp) throws Throwable{
		final Logger LOG = LoggerFactory.getLogger(jp.getTarget().getClass());
		Object o = null;
		try{
			LOG.info("Enters::"+jp.getSignature());
			o = jp.proceed();
			LOG.info("Exits::"+jp.getSignature());
		}catch (Throwable e) {
			LOG.info("@Exception::"+jp.getSignature());
			LOG.info("@Cause::"+e.getMessage());
			throw e;
		}
		return o;
		
	}
}
