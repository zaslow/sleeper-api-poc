package com.app.sleeperapipoc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import com.app.sleeperapipoc.aspects.RepositoryErrorHandlerAspect;
import com.app.sleeperapipoc.aspects.RepositoryLogAspect;
import com.app.sleeperapipoc.controllers.PlayerController;
import com.app.sleeperapipoc.controllers.TrendingPlayerController;

@EnableAspectJAutoProxy(proxyTargetClass=true)
@Configuration
@PropertySource("classpath:api.properties")
public class ApplicationConfiguration {

	@Value("${data.interval.refresh}")
	private int dataRefreshInterval;

	@Value("${data.refresh.force}")
	private boolean forceDataRefresh;

	@Value("${sleeper.api.domain}")
	private String sleeperApiDomain;
	
	@Bean
	public PlayerController playerController() {
		return new PlayerController(sleeperApiDomain);
	}

	@Bean(initMethod="init")
	public PlayerDataInitializer playerData() {
		return new PlayerDataInitializer(
			dataRefreshInterval,
			forceDataRefresh,
			playerController(),
			trendingPlayerController()
		);
	}

	@Bean
	public RepositoryErrorHandlerAspect repositoryErrorHandlerAspect() {
		return new RepositoryErrorHandlerAspect(playerData());
	}
	
	@Bean
	public RepositoryLogAspect repositoryLogAspect() {
		return new RepositoryLogAspect();
	}

	@Bean
	public TrendingPlayerController trendingPlayerController() {
		return new TrendingPlayerController(sleeperApiDomain);
	}
}
