package com.jerome.squaregamesapi.sensor.controller;

import com.jerome.squaregamesapi.sensor.HeartbeatSensor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomHeartbeat implements HeartbeatSensor {

    @Override
    public int get() {
        return new Random().nextInt(100);
    }
}
