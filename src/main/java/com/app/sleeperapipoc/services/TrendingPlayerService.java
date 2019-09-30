package com.app.sleeperapipoc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sleeperapipoc.Application;
import com.app.sleeperapipoc.controllers.RestObjectController;
import com.app.sleeperapipoc.models.TrendingPlayer;
import com.app.sleeperapipoc.repositories.TrendingPlayerRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TrendingPlayerService extends RestObjectController {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private TrendingPlayerRepository trendingPlayerRepository;
	
	public TrendingPlayerService(TrendingPlayerRepository trendingPlayerRepository) {
		this.trendingPlayerRepository = trendingPlayerRepository;
	}

	public void deleteAll() {
		trendingPlayerRepository.deleteAll();
	}
	
	public TrendingPlayer getFirst() {
		return trendingPlayerRepository.findFirstBy();
	}
	
	public void save(JsonNode player) {
		TrendingPlayer trendingPlayer = new TrendingPlayer();
		
		if (player.hasNonNull("college")) {
			String college = player.get("college").asText();
			trendingPlayer.setCollege(college);
		}

		String firstName = player.get("first_name").asText();
		trendingPlayer.setFirstName(firstName);

		String lastName = player.get("last_name").asText();
		trendingPlayer.setLastName(lastName);

		String playerId = player.get("player_id").asText();
		trendingPlayer.setPlayerId(playerId);

		String position = player.get("position").asText();
		trendingPlayer.setPosition(position);

		String team = player.hasNonNull("team") ? player.get("team").asText() : "FA";
		trendingPlayer.setTeam(team);

		log.info(String.format("Saving %3$s %4$s %1$s %2$s to database.", firstName, lastName, team, position));
		trendingPlayerRepository.save(trendingPlayer);
	}
}
