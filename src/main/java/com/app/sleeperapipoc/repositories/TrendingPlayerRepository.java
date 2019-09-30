package com.app.sleeperapipoc.repositories;

import org.springframework.data.repository.CrudRepository;

import com.app.sleeperapipoc.models.TrendingPlayer;

public interface TrendingPlayerRepository extends CrudRepository<TrendingPlayer, Integer> {
	TrendingPlayer findFirstBy();
}
