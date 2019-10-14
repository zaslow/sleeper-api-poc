package com.app.sleeperapipoc.configuration;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.sleeperapipoc.Application;
import com.app.sleeperapipoc.controllers.PlayerController;
import com.app.sleeperapipoc.controllers.TrendingPlayerController;
import com.app.sleeperapipoc.models.TrendingIdCount;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public class PlayerDataInitializer {

	private int dataRefreshInterval;
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private boolean forceDataRefresh;

	PlayerController playerController;
	
	TrendingPlayerController trendingPlayerController;
	
	public PlayerDataInitializer (
			int dataRefreshInterval,
			boolean forceDataRefresh,
			PlayerController playerController,
			TrendingPlayerController trendingPlayerController) {

		this.dataRefreshInterval = dataRefreshInterval;
		this.forceDataRefresh = forceDataRefresh;
		this.playerController = playerController;
		this.trendingPlayerController = trendingPlayerController;
	}

	public void init()
			throws JsonParseException, JsonMappingException, IOException {

		if (!forceDataRefresh) {
			long currentTime = new Date().getTime();
			long lastUpdated = trendingPlayerController.getTimeUpdated();
			long minutesSinceUpdate = ((currentTime - lastUpdated) / (60 * 1000));

			if (minutesSinceUpdate < dataRefreshInterval) {
				log.info(String.valueOf(Math.round(minutesSinceUpdate / 60)) +
					" hours since last update; trending players are up to date.");
			} else {
				update("Over 24 hours since last update.");
			}
		} else {
			update("Update requested.");
		}		
	}

	public void update(String msg)
			throws JsonParseException, JsonMappingException, IOException {

		log.info(msg);
		log.info("Fetching trending players.");

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
