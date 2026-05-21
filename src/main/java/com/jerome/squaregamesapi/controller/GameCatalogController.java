package com.jerome.squaregamesapi.controller;

import com.jerome.squaregamesapi.catalog.GameCatalog;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/catalog")
@AllArgsConstructor
public class GameCatalogController {

    private final GameCatalog gameCatalog;

    @GetMapping
    public Collection<String> getGameNames() {
        return gameCatalog.getGameNames();
    }
}
