package com.jerome.squaregamesapi.game.dao;

import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class InMemoryGameDao implements GameDao {

    // Stockage mémoire
    private final Map<String, Game> games = new HashMap<>();

    @Override
    public Stream<Game> findAll() {
        return this.games.values().stream();
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(this.games.get(gameId));
    }

    @Override
    public Game upsert(Game game) {
        this.games.put(game.getId().toString(), game);
        return game;
    }

    @Override
    public void delete(String gameId) {
        this.games.remove(gameId);
    }
}
