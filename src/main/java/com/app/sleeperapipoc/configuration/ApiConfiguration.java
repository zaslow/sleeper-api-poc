package com.app.sleeperapipoc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.app.sleeperapipoc.controllers.PlayerController;
import com.app.sleeperapipoc.controllers.TrendingPlayerController;
import com.app.sleeperapipoc.services.TrendingPlayerService;
import com.app.sleeperapipoc.services.UpdateService;

@Configuration
@PropertySource("classpath:api.properties")
public class ApiConfiguration {

	@Value("${data.interval.refresh}")
	private int dataRefreshInterval;

	@Value("${data.refresh.force}")
	private boolean forceDataRefresh;

	@Value("${sleeper.api.domain}")
	private String sleeperApiDomain;

	@Autowired
	private TrendingPlayerService trendingPlayerService;
	
	@Bean
	public PlayerController playerController() {
		return new PlayerController(sleeperApiDomain);
	}
	
	@Bean
	public TrendingPlayerController trendingPlayerController() {
		return new TrendingPlayerController(sleeperApiDomain);
	}
	
	@Bean(initMethod="checkForUpdates")
	public UpdateService updateService(
			PlayerController playerController,
			TrendingPlayerController trendingPlayerController) {

		return new UpdateService(
			dataRefreshInterval,
			forceDataRefresh,
			playerController,
			trendingPlayerController,
			trendingPlayerService
		);
	}
}
