package com.jerome.squaregamesapi.game.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameCreationParams {
    private String gameType;
    private int playerCount;
    private int boardSize;
}
