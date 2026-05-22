package com.jerome.squaregamesapi.game.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoveParams {
    private String tokenName;
    private int posX;
    private int posY;
}