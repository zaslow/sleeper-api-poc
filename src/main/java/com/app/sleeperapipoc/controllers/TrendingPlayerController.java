package com.app.sleeperapipoc.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.app.sleeperapipoc.models.TrendingIdCount;
import com.app.sleeperapipoc.services.TrendingPlayerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

public class TrendingPlayerController extends RestObjectController {

	private String apiUri;

	@Value("/trending/add")
	private String mostAddedPath;

	@Autowired
	private TrendingPlayerService trendingPlayerService;

	public TrendingPlayerController(String apiUri) {
		this.apiUri = apiUri;
	}

	public void deleteAll() {
		trendingPlayerService.deleteAll();
	}

	public List<TrendingIdCount> getAll() throws IOException {	

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		String trendingPlayers = restTemplate.getForObject(apiUri + mostAddedPath, String.class);

		return objectMapper.readValue(trendingPlayers, new TypeReference<List<TrendingIdCount>>(){});
	}

	public long getTimeUpdated() {
		return trendingPlayerService.getFirst().getLastUpdated().getTime();
	}

	public void update(JsonNode player) {
		trendingPlayerService.save(player);
	}
}
