package com.jerome.squaregamesapi.plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TaquinPlugin implements GamePlugin {

    @Value("${game.taquin.default-player-count:1}")
    private int defaultPlayerCount = 1;

    @Value("${game.taquin.default-board-size:3}")
    private int defaultBoardSize = 3;

    private final MessageSource messageSource;

    private final TaquinGameFactory taquinGameFactory = new TaquinGameFactory();

    public TaquinPlugin(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getGameId() {
        return this.taquinGameFactory.getGameFactoryId();
    }

    @Override
    public String getName(Locale locale) {
        return this.messageSource.getMessage("game.taquin.name", null, locale);
    }

    @Override
    public Game createGame() {
        return this.taquinGameFactory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public Game createGame(int playerCount, int boardSize) {
        return this.taquinGameFactory.createGame(playerCount, boardSize);
    }
}