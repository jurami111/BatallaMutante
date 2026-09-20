package com.MutantBattle.control;

import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;

// Hilo que mueve un mutante de forma continua y reporta cada movimiento al EncounterDispatcher.
// Administra su propio Thread para poder ofrecer una detencion limpia: stop() interrumpe
// el sleep en curso y awaitTermination() espera a que el hilo termine antes de continuar.
public class MutantThread implements Runnable {
    private final BaseMutant mutant;
    private final Movement movement;
    private final int fieldWidth;
    private final int fieldHeight;
    private final Team rivalTeam;
    private final EncounterDispatcher dispatcher;
    private final long stepIntervalMillis;
    private volatile boolean running = true;
    private Thread hilo;

    public MutantThread(BaseMutant mutant, Movement movement, int fieldWidth, int fieldHeight,
            Team rivalTeam, EncounterDispatcher dispatcher, long stepIntervalMillis) {
        if (mutant == null || movement == null || rivalTeam == null || dispatcher == null) {
            throw new IllegalArgumentException("El mutante, el movimiento, el equipo rival y el dispatcher no pueden ser nulos");
        }
        if (fieldWidth <= 0 || fieldHeight <= 0) {
            throw new IllegalArgumentException("El ancho y el alto deben ser positivos");
        }
        if (stepIntervalMillis <= 0) {
            throw new IllegalArgumentException("El intervalo de movimiento debe ser positivo");
        }

        this.mutant = mutant;
        this.movement = movement;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.rivalTeam = rivalTeam;
        this.dispatcher = dispatcher;
        this.stepIntervalMillis = stepIntervalMillis;
    }

    @Override
    public void run() {
        while (running && mutant.isAlive()) {
            BaseMutant enemy = findNearestEnemy();
            if (enemy != null) {
                movement.moveTowards(enemy.getPosition().getX(), enemy.getPosition().getY(), fieldWidth, fieldHeight);
            } else {
                movement.moveWithin(fieldWidth, fieldHeight);
            }
            dispatcher.reportMovement(mutant);
            esperar();
        }
    }

    // Busca el mutante vivo mas cercano del equipo rival para dirigir el movimiento hacia el
    private BaseMutant findNearestEnemy() {
        BaseMutant nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (BaseMutant candidate : rivalTeam.getMutants()) {
            if (!candidate.isAlive()) {
                continue;
            }
            double distance = mutant.getPosition().distanceTo(candidate.getPosition());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = candidate;
            }
        }
        return nearest;
    }

    // Inicia el hilo de movimiento; no hace nada si ya fue iniciado
    public synchronized void start() {
        if (hilo != null) {
            return;
        }
        hilo = new Thread(this, "MutantThread-" + mutant.getName());
        hilo.start();
    }

    // Pide al hilo que se detenga e interrumpe cualquier sleep en curso para que reaccione de inmediato
    public void stop() {
        running = false;
        if (hilo != null) {
            hilo.interrupt();
        }
    }

    // Bloquea al llamador hasta que el hilo del mutante haya terminado por completo
    public void awaitTermination() {
        if (hilo == null) {
            return;
        }
        try {
            hilo.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void esperar() {
        try {
            Thread.sleep(stepIntervalMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }
}

