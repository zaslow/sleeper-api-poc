package com.app.sleeperapipoc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrendingIdCount {

	private Integer count;

	@JsonProperty("player_id")
	private String playerId;

	public Integer getCount() {
		return count;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
