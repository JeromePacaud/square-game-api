package com.jerome.squaregamesapi.catalog;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/catalog")
@AllArgsConstructor
public class GameCatalogController {
    private final GameCatalog gameCatalog;

    @GetMapping
    public Collection<String> getGameNames(@RequestHeader(name = "Accept-Language", defaultValue = "fr") Locale locale) {
        return gameCatalog.getGameNames(locale);
    }

}
