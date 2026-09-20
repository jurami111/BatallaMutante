package com.MutantBattle.control;

import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Detecta encuentros entre mutantes de equipos rivales y resuelve cada par en rango como una tarea
// independiente en un pool de hilos. Cada par lo dispara solo el mutante de menor id, de modo que
// se produce una unica accion por par en cada tick de movimiento aunque ambos reporten a la vez;
// Combat.resolve ademas bloquea ambos mutantes en un orden fijo para evitar deadlocks cuando un
// mismo mutante participa en varios encuentros a la vez.
public class EncounterDispatcher {
    private final Combat combat;
    private final Team team1;
    private final Team team2;
    private final ExecutorService encounterExecutor;

    public EncounterDispatcher(Combat combat, Team team1, Team team2, int poolSize) {
        if (combat == null || team1 == null || team2 == null) {
            throw new IllegalArgumentException("El combate y los equipos no pueden ser nulos");
        }
        if (poolSize <= 0) {
            throw new IllegalArgumentException("El tamano del pool debe ser positivo");
        }

        this.combat = combat;
        this.team1 = team1;
        this.team2 = team2;
        this.encounterExecutor = Executors.newFixedThreadPool(poolSize);
    }

    public void reportMovement(BaseMutant mover) {
        if (!mover.isAlive()) {
            return;
        }

        Team rivalTeam = mover.getTeam() == team1 ? team2 : team1;
        for (BaseMutant rival : rivalTeam.getMutants()) {
            actualizarEncuentro(mover, rival);
        }
    }

    public void shutdown() {
        encounterExecutor.shutdown();
        try {
            if (!encounterExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                encounterExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            encounterExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void actualizarEncuentro(BaseMutant mover, BaseMutant rival) {
        // Solo el mutante de menor id dispara la accion del par: asi cada par produce una unica
        // accion por tick aunque los dos reporten su movimiento en el mismo instante
        if (mover.getId() > rival.getId()) {
            return;
        }
        if (combat.areInRange(mover, rival)) {
            encounterExecutor.submit(() -> combat.resolve(mover, rival));
        }
    }
}
