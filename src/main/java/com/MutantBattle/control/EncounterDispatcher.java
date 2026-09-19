package com.MutantBattle.control;

import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Detecta encuentros entre mutantes de equipos rivales y resuelve cada par como una tarea
// independiente en un pool de hilos. Mientras un par permanece dentro del radio solo se dispara
// una accion (se libera al salir de rango), evitando ataques duplicados en cada tick de
// movimiento; Combat.resolve ademas bloquea ambos mutantes en un orden fijo para evitar deadlocks
// cuando un mismo mutante participa en varios encuentros a la vez.
public class EncounterDispatcher {
    private final Combat combat;
    private final Team team1;
    private final Team team2;
    private final ExecutorService encounterExecutor;
    private final ConcurrentHashMap<String, Boolean> encuentrosActivos = new ConcurrentHashMap<>();

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
        String pairKey = pairKey(mover, rival);

        if (!combat.areInRange(mover, rival)) {
            // Al salir del radio se libera el par para permitir una nueva accion si vuelven a entrar
            encuentrosActivos.remove(pairKey);
            return;
        }

        // putIfAbsent es atomico: solo el primer hilo que detecta la entrada dispara la accion,
        // el resto de los ticks mientras siguen en rango no generan ataques repetidos
        if (encuentrosActivos.putIfAbsent(pairKey, Boolean.TRUE) == null) {
            encounterExecutor.submit(() -> combat.resolve(mover, rival));
        }
    }

    private String pairKey(BaseMutant first, BaseMutant second) {
        long firstId = first.getId();
        long secondId = second.getId();
        return firstId <= secondId ? (firstId + "|" + secondId) : (secondId + "|" + firstId);
    }
}
