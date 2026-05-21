package com.jerome.squaregamesapi.catalog;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GameCatalogImpl implements GameCatalog {

    @Override
    public Collection<String> getGameNames() {
        return List.of(
                new TicTacToeGameFactory().getGameFactoryId()
        );
    }
}
