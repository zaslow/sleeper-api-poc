package com.app.sleeperapipoc.services;

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

public class UpdateService {
	
	private int dataRefreshInterval;
	
	private boolean forceDataRefresh;

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private PlayerController playerController;

	private TrendingPlayerController trendingPlayerController;

	private TrendingPlayerService trendingPlayerService;

	public UpdateService(
			int dataRefreshInterval,
			boolean forceDataRefresh,
			PlayerController playerController,
			TrendingPlayerController trendingPlayerController,
			TrendingPlayerService trendingPlayerService) {

		this.dataRefreshInterval = dataRefreshInterval;
		this.forceDataRefresh = forceDataRefresh;
		this.playerController = playerController;
		this.trendingPlayerController = trendingPlayerController;
		this.trendingPlayerService = trendingPlayerService;
	}
	
	public void checkForUpdates() throws JsonParseException, JsonMappingException, IOException {	
		
		if (!forceDataRefresh) {
			long currentTime = new Date().getTime();
			long lastUpdated = trendingPlayerService.getFirst().getLastUpdated().getTime();
			long minutesSinceUpdate = ((currentTime - lastUpdated) / (60 * 1000));
			
			if (minutesSinceUpdate < dataRefreshInterval) {
				log.info(String.valueOf(Math.round(minutesSinceUpdate / 60)) +
					" hours since last update; Trending players are up to date.");
			} else {
				log.info("Over 24 hours since last update.");
				refresh();
			}
		} else {
			log.info("Update requested.");
			refresh();
		}
	}
	
	private void refresh()
			throws JsonParseException, JsonMappingException, IOException {

		log.info("Refreshing trending players.");

		trendingPlayerService.deleteAll();
		
		JsonNode allPlayers = playerController.getAll();
		
		List<TrendingIdCount> trendingPlayers = trendingPlayerController.getAll();
		
		for (TrendingIdCount trendingIdCount : trendingPlayers) {
			String playerId = trendingIdCount.getPlayerId();
			JsonNode selectedPlayer = allPlayers.get(playerId);
			trendingPlayerService.save(selectedPlayer);
		}
	}	
}
