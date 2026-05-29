package com.jerome.squaregamesapi.game.dao;

import com.jerome.squaregamesapi.game.entity.GameEntity;
import com.jerome.squaregamesapi.game.entity.GameTokenEntity;
import com.jerome.squaregamesapi.game.repository.GameEntityRepository;
import com.jerome.squaregamesapi.plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.Game;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
@Primary
public class JpaGameDao implements GameDao {
    private GameEntityRepository gameEntityRepository;
    private List<GamePlugin> gamePlugins;

    @Override
    public Stream<Game> findAll() {
        return gameEntityRepository.findAll().stream().map(this::toGame);
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return gameEntityRepository.findById(gameId).map(this::toGame);
    }

    @Override
    public Game upsert(Game game) {
        gameEntityRepository.save(toEntity(game));
        return game;
    }

    @Override
    public void delete(String gameId) {
        gameEntityRepository.deleteById(gameId);
    }

    private GameEntity toEntity(Game game) {

        // Créer une nouvelle instance de GameEntity
        GameEntity gameEntity = new GameEntity();

        // Assigner game.getId().toString() → entity.id
        gameEntity.id = game.getId().toString();

        // Assigner game.getFactoryId() → entity.factoryId
        gameEntity.factoryId = game.getFactoryId();

        // Assigner game.getBoardSize() → entity.boardSize
        gameEntity.boardSize = game.getBoardSize();

        // Convertir Set<UUID> des joueurs en String séparée par virgules → entity.playerIds
        gameEntity.playerIds = game.getPlayerIds().stream().map(UUID::toString).collect(Collectors.joining(","));

        // Pour les tokens : parcourir game.getBoard() et game.getRemainingTokens()
        //   → pour chaque token, créer un GameTokenEntity
        //   → assigner ownerId, name, removed, x (position), y (position)
        game.getBoard().values().forEach(token -> {
            GameTokenEntity gameTokenEntity = new GameTokenEntity();
            gameTokenEntity.ownerId = token.getOwnerId().toString();
            gameTokenEntity.name = token.getName();
            gameTokenEntity.removed = false;
            gameTokenEntity.x = token.getPosition().x();
            gameTokenEntity.y = token.getPosition().y();
            gameEntity.tokens.add(gameTokenEntity);
        });

        game.getRemainingTokens().forEach(token -> {
            GameTokenEntity gameTokenEntity = new GameTokenEntity();
            gameTokenEntity.ownerId = token.getOwnerId().toString();
            gameTokenEntity.name = token.getName();
            gameTokenEntity.removed = true;
            gameTokenEntity.x = null;
            gameTokenEntity.y = null;
            gameEntity.tokens.add(gameTokenEntity);
        });

        // Retourner l'entité
        return gameEntity;
    }

    private Game toGame(GameEntity gameEntity) {
        int playerCount;

        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getGameId().equals(gameEntity.factoryId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Plugin introuvable pour l'id: " + gameEntity.factoryId));

        if (gameEntity.playerIds.isEmpty()) {
            playerCount = 0;
        } else {
            playerCount = gameEntity.playerIds.split(",").length;
        }

        return plugin.createGame(playerCount, gameEntity.boardSize);
    }
}
