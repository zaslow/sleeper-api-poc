package com.app.sleeperapipoc.configuration;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import com.app.sleeperapipoc.Application;
import com.app.sleeperapipoc.aspects.RepositoryLogAspect;
import com.app.sleeperapipoc.controllers.PlayerController;
import com.app.sleeperapipoc.controllers.TrendingPlayerController;
import com.app.sleeperapipoc.models.TrendingIdCount;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@EnableAspectJAutoProxy(proxyTargetClass=true)
@Configuration
@PropertySource("classpath:api.properties")
public class ApiConfiguration {

	@Value("${data.interval.refresh}")
	private int dataRefreshInterval;

	@Value("${data.refresh.force}")
	private boolean forceDataRefresh;

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Value("${sleeper.api.domain}")
	private String sleeperApiDomain;
	
	@Bean
	public PlayerController playerController() {
		return new PlayerController(sleeperApiDomain);
	}
	
	@Bean
	public RepositoryLogAspect restLogAspect() {
		return new RepositoryLogAspect();
	}

	@Bean
	public TrendingPlayerController trendingPlayerController() {
		return new TrendingPlayerController(sleeperApiDomain);
	}

	@Bean
	public CommandLineRunner checkForUpdate(
			PlayerController playerController,
			TrendingPlayerController trendingPlayerController)
			throws JsonParseException, JsonMappingException, IOException {

		return (args) -> {
			if (!forceDataRefresh) {
				long currentTime = new Date().getTime();
				long lastUpdated = trendingPlayerController.getTimeUpdated();
				long minutesSinceUpdate = ((currentTime - lastUpdated) / (60 * 1000));

				if (minutesSinceUpdate < dataRefreshInterval) {
					log.info(String.valueOf(Math.round(minutesSinceUpdate / 60)) +
						" hours since last update; Trending players are up to date.");
				} else {
					log.info("Over 24 hours since last update.");
					refresh(playerController, trendingPlayerController);
				}
			} else {
				log.info("Update requested.");
				refresh(playerController, trendingPlayerController);
			}
		};
	}

	private void refresh(
			PlayerController playerController,
			TrendingPlayerController trendingPlayerController)
			throws JsonParseException, JsonMappingException, IOException {

		log.info("Refreshing trending players.");

		trendingPlayerController.deleteAll();

		JsonNode allPlayers = playerController.getAll();

		List<TrendingIdCount> trendingPlayers = trendingPlayerController.getAll();

		for (TrendingIdCount trendingIdCount : trendingPlayers) {
			String playerId = trendingIdCount.getPlayerId();
			JsonNode selectedPlayer = allPlayers.get(playerId);

			trendingPlayerController.update(selectedPlayer);
		}
	}
}
