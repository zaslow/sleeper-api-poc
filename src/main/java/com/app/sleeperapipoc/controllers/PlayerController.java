package com.app.sleeperapipoc.controllers;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public class PlayerController extends RestObjectController {
	
	private String apiUri;
	
	public PlayerController(String apiUri) {
		this.apiUri = apiUri;
	}

	public JsonNode getAll() throws IOException {

		String allPlayers = restTemplate.getForObject(apiUri, String.class);

		return objectMapper.readTree(allPlayers);
	}
}
