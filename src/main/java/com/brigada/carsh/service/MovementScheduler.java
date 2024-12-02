package com.brigada.carsh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovementScheduler {
    private final CarMovementSimulator simulator;

    @Scheduled(initialDelay = 15000, fixedRate = 15000)
    public void updateCarLocations() {
        simulator.simulateMovement();
    }
}
