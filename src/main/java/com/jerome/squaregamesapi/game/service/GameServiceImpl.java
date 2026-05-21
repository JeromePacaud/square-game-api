package com.jerome.squaregamesapi.game.service;

import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {


    // Stockage en mémoire des parties en cours
    private final Map<UUID, Game> games = new HashMap<>();

    // Stockage des factories disponibles
    private final Map<String, GameFactory> gameFactories = Map.of(
            "TicTacToe", new TicTacToeGameFactory(),
            "Taquin", new TaquinGameFactory(),
            "ConnectFour", new ConnectFourGameFactory()
    );

    @Override
    public Game createGame(String gameType, int playerCount, int boardSize) {
        GameFactory gameFactory = gameFactories.get(gameType);

        if (gameFactory == null) {
            throw new RuntimeException("Type de jeu inconnu : " + gameType);
        }

        Game game = gameFactory.createGame(playerCount, boardSize);

        // Enregistrer le jeu avec un ID unique dans games
        games.put(game.getId(), game);

        return game;
    }

    @Override
    public Game getGame(UUID gameId) {
        // Récupérer le jeu dans games
        Game game = games.get(gameId);

        if (game == null) {
            throw new RuntimeException("Jeu non trouvé : " + gameId);
        }

        return game;
    }

    @Override
    public Collection<CellPosition> getMoves(UUID gameId) {
        Game game = this.getGame(gameId);
        Collection<CellPosition> moves = new ArrayList<>();

        for (Token token : game.getRemainingTokens()) {
            moves.addAll(token.getAllowedMoves());
        }

        for (Token token : game.getBoard().values()) {
            moves.addAll(token.getAllowedMoves());
        }

        return moves;
    }

    @Override
    public Game play(UUID gameId, String tokenName, int posX, int posY) throws InvalidPositionException {
        Game game = getGame(gameId);

        // Rechercher le jeton correspondant au nom dans les jetons restants du jeu
        Token foundToken = null;

        // On parcourt les jetons restants pour trouver celui qui correspond au nom donné.
        for(Token token : game.getRemainingTokens()) {
            if(token.getName().equals(tokenName)) {
                foundToken = token;
                break;
            }
        }

        /*
        On vérifie si on a trouvé le jeton dans les jetons restants. Si ce n'est pas le cas, on peut aussi
        chercher dans les jetons déjà joués (au cas où le client envoie un nom de jeton qui a déjà été joué).
        */
        if(foundToken == null) {
            for(Token token : game.getRemainingTokens()) {
                if(token.getName().equals(tokenName)) {
                    foundToken = token;
                    break;
                }
            }
        }

        // si après les deux recherches on a toujours rien trouvé
        if(foundToken == null) {
            throw new RuntimeException("Jeton non trouvé : " + tokenName);
        }

        CellPosition position = new CellPosition(posX, posY);
        foundToken.moveTo(position);

        return game;
    }
}
