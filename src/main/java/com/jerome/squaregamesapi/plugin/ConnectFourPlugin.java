package com.jerome.squaregamesapi.plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ConnectFourPlugin implements GamePlugin {

    @Value("${game.connectfour.default-player-count:2}")
    private int defaultPlayerCount = 2;

    @Value("${game.connectfour.default-board-size:7}")
    private int defaultBoardSize = 7;

    private final MessageSource messageSource;

    private final ConnectFourGameFactory connectFourGameFactory = new ConnectFourGameFactory();

    public ConnectFourPlugin(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getGameId() {
        return this.connectFourGameFactory.getGameFactoryId();
    }

    @Override
    public String getName(Locale locale) {
        return this.messageSource.getMessage("game.connectfour.name", null, locale);
    }

    @Override
    public Game createGame() {
        return this.connectFourGameFactory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public Game createGame(int playerCount, int boardSize) {
        return this.connectFourGameFactory.createGame(playerCount, boardSize);
    }
}