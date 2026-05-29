package com.jerome.squaregamesapi.game.repository;

import com.jerome.squaregamesapi.game.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GameEntityRepository extends JpaRepository<GameEntity, String> {
    // On peut créer des requettes automatiquement dans les répo JPA
    // Collection<GameEntity> findByUsername(String username);
}
