package com.app.sleeperapipoc.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.app.sleeperapipoc.configuration.PlayerDataInitializer;

@Aspect
public class RepositoryErrorHandlerAspect {

	private PlayerDataInitializer playerData;
	
	public RepositoryErrorHandlerAspect(PlayerDataInitializer playerData) {
		this.playerData = playerData;
	}
	
	@Around("execution(* com.app.sleeperapipoc.repositories.*PlayerRepository.find*(..))")
	public Object catchNullFinds(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		if (proceedingJoinPoint.proceed() == null) {
			playerData.update("No player data detected.");
		}

		return proceedingJoinPoint.proceed();
	}
}
