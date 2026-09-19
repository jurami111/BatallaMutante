package com.MutantBattle.control;

import com.MutantBattle.model.BaseMutant;

// Hilo que mueve un mutante de forma continua y reporta cada movimiento al EncounterDispatcher.
// Administra su propio Thread para poder ofrecer una detencion limpia: stop() interrumpe
// el sleep en curso y awaitTermination() espera a que el hilo termine antes de continuar.
public class MutantThread implements Runnable {
    private final BaseMutant mutant;
    private final Movement movement;
    private final int fieldWidth;
    private final int fieldHeight;
    private final EncounterDispatcher dispatcher;
    private final long stepIntervalMillis;
    private volatile boolean running = true;
    private Thread hilo;

    public MutantThread(BaseMutant mutant, Movement movement, int fieldWidth, int fieldHeight,
            EncounterDispatcher dispatcher, long stepIntervalMillis) {
        if (mutant == null || movement == null || dispatcher == null) {
            throw new IllegalArgumentException("El mutante, el movimiento y el dispatcher no pueden ser nulos");
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
        this.dispatcher = dispatcher;
        this.stepIntervalMillis = stepIntervalMillis;
    }

    @Override
    public void run() {
        while (running && mutant.isAlive()) {
            movement.moveWithin(fieldWidth, fieldHeight);
            dispatcher.reportMovement(mutant);
            esperar();
        }
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

