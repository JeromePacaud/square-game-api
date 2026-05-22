package com.jerome.squaregamesapi.plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;

import java.util.Locale;

public interface GamePlugin {

    // Identifiant du jeu (ex: "tictactoe")
    String getGameId();

    // Nom traduit du jeu selon la locale
    String getName(Locale locale);

    // Crée une partie avec les paramètres par défaut
    Game createGame();

    // Crée une partie avec des paramètres personnalisés
    Game createGame(int playerCount, int boardSize);
}