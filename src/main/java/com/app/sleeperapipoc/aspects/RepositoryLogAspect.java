package com.app.sleeperapipoc.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.sleeperapipoc.Application;

@Aspect
public class RepositoryLogAspect {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@AfterReturning("execution(* com.app.sleeperapipoc.repositories.*Repository.save*(..))")
	public void logUpdate(final JoinPoint joinPoint) {
		log.info("-> Successfully saved " + joinPoint.getKind() + " to DB.");
	}
}
