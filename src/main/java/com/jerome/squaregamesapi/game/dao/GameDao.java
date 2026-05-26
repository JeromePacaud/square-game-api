package com.jerome.squaregamesapi.game.dao;

import fr.le_campus_numerique.square_games.engine.Game;

import java.util.Optional;
import java.util.stream.Stream;

public interface GameDao {
    // Retourne toutes les parties
    Stream<Game> findAll();

    // Retourne une partie par son id
    Optional<Game> findById(String gameId);

    // Créer ou met à jour une partie
    Game upsert(Game game);

    // Supprimer une partie
    void delete(String gameId);
}
