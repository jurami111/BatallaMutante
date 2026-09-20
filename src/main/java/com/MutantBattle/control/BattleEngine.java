package com.MutantBattle.control;

import com.MutantBattle.config.constants;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import java.util.ArrayList;
import java.util.List;

// Orquesta una batalla completa: mueve los mutantes en hilos paralelos, procesa sus encuentros,
// detecta la condicion de finalizacion (un equipo se queda sin mutantes vivos) y calcula el ganador
public class BattleEngine {
    private final Battlefield battlefield;
    private final Combat combat;
    private final long stepIntervalMillis;
    private final long pollIntervalMillis;

    public BattleEngine(Battlefield battlefield) {
        this(battlefield, new Combat(constants.ENCOUNTER_RADIUS), constants.MOVEMENT_STEP_INTERVAL_MILLISECONDS);
    }

    public BattleEngine(Battlefield battlefield, Combat combat, long stepIntervalMillis) {
        if (battlefield == null || combat == null) {
            throw new IllegalArgumentException("El campo de batalla y el combate no pueden ser nulos");
        }
        if (stepIntervalMillis <= 0) {
            throw new IllegalArgumentException("El intervalo de movimiento debe ser positivo");
        }

        this.battlefield = battlefield;
        this.combat = combat;
        this.stepIntervalMillis = stepIntervalMillis;
        this.pollIntervalMillis = stepIntervalMillis;
    }

    // Ejecuta la batalla hasta que un equipo pierda todos sus mutantes y devuelve al ganador (o null si empatan)
    public Team runUntilFinished() {
        Team team1 = battlefield.getTeam1();
        Team team2 = battlefield.getTeam2();
        EncounterDispatcher dispatcher = new EncounterDispatcher(
                combat, team1, team2, constants.ENCOUNTER_POOL_SIZE);

        List<MutantThread> hilos = crearHilos(team1, team2, dispatcher);
        for (MutantThread hilo : hilos) {
            hilo.start();
        }

        while (!battlefield.isFinished()) {
            esperar(pollIntervalMillis);
        }

        detener(hilos, dispatcher);
        return battlefield.getWinner();
    }

    private List<MutantThread> crearHilos(Team team1, Team team2, EncounterDispatcher dispatcher) {
        List<MutantThread> hilos = new ArrayList<>();
        for (BaseMutant mutante : team1.getMutants()) {
            hilos.add(crearHilo(mutante, team2, dispatcher));
        }
        for (BaseMutant mutante : team2.getMutants()) {
            hilos.add(crearHilo(mutante, team1, dispatcher));
        }
        return hilos;
    }

    private MutantThread crearHilo(BaseMutant mutante, Team rival, EncounterDispatcher dispatcher) {
        return new MutantThread(mutante,
                new Movement(mutante.getPosition(), constants.MUTANT_SPEED),
                battlefield.getWidth(), battlefield.getHeight(),
                rival, dispatcher, stepIntervalMillis);
    }

    private void detener(List<MutantThread> hilos, EncounterDispatcher dispatcher) {
        for (MutantThread hilo : hilos) {
            hilo.stop();
        }
        for (MutantThread hilo : hilos) {
            hilo.awaitTermination();
        }
        dispatcher.shutdown();
    }

    private void esperar(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
