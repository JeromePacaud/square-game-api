package com.jerome.squaregamesapi.game.service;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;

import java.util.Collection;
import java.util.UUID;

public interface GameService {
    Game createGame(String gameType, int playerCount, int boardSize);
    Game getGame(UUID gameId);
    Collection<CellPosition> getMoves(UUID gameId);
    Game play(UUID gameId, String tokenName, int posX, int posY) throws InvalidPositionException;
}
