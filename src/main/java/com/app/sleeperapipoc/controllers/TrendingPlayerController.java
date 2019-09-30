package com.app.sleeperapipoc.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.app.sleeperapipoc.models.TrendingIdCount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class TrendingPlayerController extends RestObjectController {

	private String apiUri;

	@Value("/trending/add")
	private String mostAddedPath;
	
	public TrendingPlayerController(String apiUri) {
		this.apiUri = apiUri;
	}

	public List<TrendingIdCount> getAll() throws IOException {	

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		String trendingPlayers = restTemplate.getForObject(apiUri + mostAddedPath, String.class);

		return objectMapper.readValue(trendingPlayers, new TypeReference<List<TrendingIdCount>>(){});
	}
}
