package com.jerome.squaregamesapi.game.controller;

import com.jerome.squaregamesapi.game.dto.GameCreationParams;
import com.jerome.squaregamesapi.game.dto.MoveParams;
import com.jerome.squaregamesapi.game.service.GameService;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/games")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    // Créer une nouvelle partie
    @PostMapping
    public Game createGame(@RequestBody GameCreationParams params, @RequestHeader("X-UserId")  UUID userId) {
        return gameService.createGame(params.getGameType(), params.getPlayerCount(), params.getBoardSize(), userId);
    }

    // Récupère l'état d'une partie
    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable UUID gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping()
    public Collection<Game> getGames(@RequestHeader("X-UserId")  UUID userId) {
        return gameService.getGames(userId);
    }

    // Liste les coups possible
    @GetMapping("/{gameId}/moves")
    public Collection<CellPosition> getMoves(@PathVariable UUID gameId) {
        return gameService.getMoves(gameId);
    }

    // Joue un coup
    @PostMapping("/{gameId}/moves")
    public Game play(@PathVariable UUID gameId, @RequestBody MoveParams params, @RequestHeader("X-UserId") UUID userId) throws InvalidPositionException {
        return gameService.play(
                gameId,
                params.getTokenName(),
                params.getPosX(),
                params.getPosY(),
                userId
        );

    }
}
