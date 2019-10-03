package com.app.sleeperapipoc.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.sleeperapipoc.Application;

@Aspect
public class RepositoryLogAspect {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Pointcut("execution(* com.app.sleeperapipoc.repositories.*Repository.save*(..))")
	public void repositorySavePointcut() {}

	@Before("repositorySavePointcut()")
	public void logRest(final JoinPoint joinPoint) {
		log.info("-> Saving object of " + joinPoint.getArgs()[0].getClass() + " to DB.");
	}
}
