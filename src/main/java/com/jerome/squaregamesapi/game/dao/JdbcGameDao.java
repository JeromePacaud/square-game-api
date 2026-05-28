package com.jerome.squaregamesapi.game.dao;

import com.jerome.squaregamesapi.plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.Game;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
@AllArgsConstructor
public class JdbcGameDao implements GameDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final List<GamePlugin> gamePlugins;

    @Override
    public Stream<Game> findAll() {
        // Écrire une requête SQL : SELECT * FROM game
        String sql = "SELECT * FROM game";

        // Utiliser jdbcTemplate.query() avec la requête et un RowMapper
        // Le RowMapper reçoit un ResultSet et retourne un objet Game
        //  → extraire chaque colonne avec resultSet.getString("id"), etc.
        List<Game> game = jdbcTemplate.query(sql, (rs, rowNum) -> {
            GamePlugin foundPlugin = null;
            String id = rs.getString("id");
            String type = rs.getString("type");
            int board_size = rs.getInt("board_size");
            int player_count =rs.getInt("player_count");
            String[] ids = rs.getString("player_ids").split(",");

            for (GamePlugin plugin : gamePlugins) {
                if (plugin.getGameId().equals(type)) {
                    foundPlugin = plugin;
                    break;
                }
            }

            if (foundPlugin == null) {
                throw new RuntimeException("Type de jeu non trouvé : " + type);
            }

            return foundPlugin.createGame(player_count, board_size);
        });
        // Retourner le résultat sous forme de Stream
        return Stream.of(
                game.toArray(new Game[0])
        );
    }

    @Override
    public Optional<Game> findById(String gameId) {
        // Écrire une requête SQL : SELECT * FROM game WHERE id = :id
        String sql = "SELECT * FROM game WHERE id = :gameId";
        // Créer une Map avec la clé "id" et la valeur gameId (ce sont les named parameters)
        Map<String, String> gameIdentifier = new HashMap<>();
        gameIdentifier.put("gameId", gameId);
        // Utiliser jdbcTemplate.query() avec la requête, la Map et le RowMapper
        List<Game> games = jdbcTemplate.query(sql, gameIdentifier, (rs, rowNum) -> {
            GamePlugin foundPlugin = null;
            String id = rs.getString("id");
            String type = rs.getString("type");
            int board_size = rs.getInt("board_size");
            int player_count =rs.getInt("player_count");
            String[] ids = rs.getString("player_ids").split(",");

            for (GamePlugin plugin : gamePlugins) {
                if (plugin.getGameId().equals(type)) {
                    foundPlugin = plugin;
                    break;
                }
            }

            if (foundPlugin == null) {
                throw new RuntimeException("Type de jeu non trouvé : " + type);
            }

            return foundPlugin.createGame(player_count, board_size);
        });
        // Le résultat est une liste → retourner le premier élément dans un Optional
        //   → si la liste est vide → Optional.empty()
        if (games.isEmpty()) {
            return Optional.empty();
        }
        //   → sinon → Optional.of(liste.get(0))
        return Optional.of(games.getFirst());
    }

    @Override
    public Game upsert(Game game) {
        // Écrire une requête SQL INSERT ... ON DUPLICATE KEY upsert ...
        String sql = "INSERT INTO game(id, type, player_count, board_size, player_ids)" +
                " VALUES (:id, :type, :player_count, :board_size, :player_ids)" +
                " ON DUPLICATE KEY UPDATE" +
                " player_count = :player_count," +
                " board_size = :board_size," +
                " player_ids = :player_ids";
        // Créer une Map avec toutes les valeurs de la partie (id, type, playerCount, etc.)
        Map<String, Object> gameData = new HashMap<>();
        gameData.put("id", game.getId().toString());
        gameData.put("type", game.getFactoryId());
        gameData.put("player_count", game.getPlayerIds().size());
        gameData.put("board_size", game.getBoardSize());
        gameData.put("player_ids",
                game.getPlayerIds()
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.joining(","))
        );

        // Utiliser jdbcTemplate.update() avec la requête et la Map
        jdbcTemplate.update(sql, gameData);
        // Retourner le game
        return game;
    }

    @Override
    public void delete(String gameId) {
        // Écrire une requête SQL : DELETE FROM game WHERE id = :id
        String SQL = "DELETE FROM game WHERE id = :gameId";
        // Créer une Map avec la clé "id" et la valeur gameId
        Map<String, String> gameIdentifier = new HashMap<>();
        gameIdentifier.put("gameId", gameId);

        // Utiliser jdbcTemplate.update() avec la requête et la Map
        jdbcTemplate.update(SQL, gameIdentifier);
    }
}
