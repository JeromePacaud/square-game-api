package com.jerome.squaregamesapi.catalog;

import com.jerome.squaregamesapi.plugin.GamePlugin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class GameCatalogImpl implements GameCatalog {

    private final List<GamePlugin> gamePlugins;

    @Override
    public Collection<String> getGameNames(Locale locale) {
        /*
        return this.gamePlugins.stream()
                .map(plugin -> plugin.getName(locale))
                .collect(Collectors.toList());

        */
        Collection<String> gameNames = new ArrayList<>();

        for (GamePlugin gamePlugin : this.gamePlugins) {
            gameNames.add(gamePlugin.getName(locale));
        }
        return gameNames;
    }
}
