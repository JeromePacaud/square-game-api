package com.jerome.squaregamesapi.game.service;

import com.jerome.squaregamesapi.game.dao.GameDao;
import com.jerome.squaregamesapi.plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    // Injection automatique par spring de tous les @Component qui implémentent GamePlugin
    private final List<GamePlugin> gamePlugins;

    // Stockage en mémoire des parties en cours
    private final GameDao gameDao;

    @Override
    public Game createGame(String gameType, int playerCount, int boardSize) {
        // Récupérer le plugin correspondant au type de jeu demandé
        GamePlugin foundPlugin = null;

        for (GamePlugin plugin : gamePlugins) {
            if (plugin.getGameId().equals(gameType)) {
                foundPlugin = plugin;
                break;
            }
        }

        // Si aucun plugin n'est trouvé pour le type de jeu demandé
        if (foundPlugin == null) {
            throw new RuntimeException("Type de jeu non trouvé : " + gameType);
        }

        // Si le plugin est trouvé, on crée la partie
        Game game;
        if (playerCount == 0 || boardSize == 0) {
            game = foundPlugin.createGame();
        } else {
            game = foundPlugin.createGame(playerCount, boardSize);
        }

        return gameDao.upsert(game);
    }

    @Override
    public Game getGame(UUID gameId) {
        return gameDao.findById(gameId.toString())
                .orElseThrow(() -> new RuntimeException("Jeu non trouvée : " + gameId));
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
        Game game = this.getGame(gameId);

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
            for(Token token : game.getBoard().values()) {
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
