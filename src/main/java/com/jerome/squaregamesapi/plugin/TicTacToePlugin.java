package com.jerome.squaregamesapi.plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TicTacToePlugin implements GamePlugin {

    // Valeurs injectées depuis application.properties
    @Value("${game.tictactoe.default-player-count:2}")
    private int defaultPlayerCount;

    @Value("${game.tictactoe.default-board-size:3}")
    private int defaultBoardSize;

    private final MessageSource messageSource;


    private final TicTacToeGameFactory factory = new TicTacToeGameFactory();

    public TicTacToePlugin(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getGameId() {
        return factory.getGameFactoryId();
    }

    @Override
    public String getName(Locale locale) {
        return this.messageSource.getMessage("game.tictactoe.name", null, locale);
    }

    @Override
    public Game createGame() {
        return factory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public Game createGame(int playerCount, int boardSize) {
        return factory.createGame(playerCount, boardSize);
    }
}