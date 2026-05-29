package com.jerome.squaregamesapi.game.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "game")
public class GameEntity {
    @Id public String id;
    public String factoryId;
    public int boardSize;
    public String name;
    public String playerIds;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> tokens = new ArrayList<>();
}
