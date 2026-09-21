package com.MutantBattle.control;

import com.MutantBattle.config.constants;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.ui.Console;
//Ui Enhancement
import com.MutantBattle.ui.EventLog;
//Ui Enhancement
import java.util.Random;

public class Combat { // Clase que representa el combate entre mutantes
    private final int detectionRadius;
    private final Random random;

    public Combat() { // Constructor que utiliza el radio de detección por defecto
        this(constants.ENCOUNTER_RADIUS, new Random());
    }

    public Combat(int detectionRadius) { // Constructor que permite especificar el radio de detección
        this(detectionRadius, new Random());
    }

    public Combat(int detectionRadius, Random random) {
        if (detectionRadius <= 0) {
            throw new IllegalArgumentException("El radio de deteccion debe ser positivo");
        }
        if (random == null) {
            throw new IllegalArgumentException("La fuente aleatoria no puede ser nula");
        }
        this.detectionRadius = detectionRadius;
        this.random = random;
    }

    public int getDetectionRadius() { // Devuelve el radio de detección utilizado en el combate
        return detectionRadius;
    }

    public boolean areInRange(BaseMutant firstMutant, BaseMutant secondMutant) { // Devuelve true si los mutantes están dentro del radio de detección
        if (!sonMutantesValidos(firstMutant, secondMutant)) {
            return false;
        }

        return firstMutant.getPosition().distanceTo(secondMutant.getPosition()) <= detectionRadius;
    }

    public void resolve(BaseMutant firstMutant, BaseMutant secondMutant) {
        if (!areInRange(firstMutant, secondMutant)) {
            return;
        }

        // Bloquea siempre en el mismo orden (por id) para evitar deadlocks entre encuentros concurrentes
        BaseMutant primerCandado = firstMutant.getId() < secondMutant.getId() ? firstMutant : secondMutant;
        BaseMutant segundoCandado = primerCandado == firstMutant ? secondMutant : firstMutant;

        synchronized (primerCandado) {
            synchronized (segundoCandado) {
                if (!firstMutant.isAlive() || !secondMutant.isAlive()) {
                    return;
                }

                CombatDecision firstDecision = decidir();
                CombatDecision secondDecision = decidir();
                ejecutarAtaque(firstMutant, secondMutant, firstDecision, secondDecision);
                ejecutarAtaque(secondMutant, firstMutant, secondDecision, firstDecision);
            }
        }
    }

    private CombatDecision decidir() {
        return random.nextBoolean() ? CombatDecision.ATTACK : CombatDecision.DEFEND;
    }

    private void ejecutarAtaque(BaseMutant atacante, BaseMutant objetivo,
            CombatDecision decisionAtacante, CombatDecision decisionObjetivo) {
        if (decisionAtacante != CombatDecision.ATTACK
                || !atacante.isAlive() || !objetivo.isAlive()) {
            return;
        }
        if (atacante.getPower() == null) {
            return;
        }

        int energiaAnterior = objetivo.getEnergy();
        int dano = calcularDano(atacante, objetivo, decisionObjetivo);
        objetivo.receiveDamage(dano);

        if (objetivo.getEnergy() < energiaAnterior) {
            Console.logEncounter(atacante, objetivo, dano);
            //Ui Enhancement
            EventLog.get().log(atacante.getName() + " hit " + objetivo.getName() + " for " + dano,
                    atacante.getTeam() != null ? atacante.getTeam().getColor() : "");
            //Ui Enhancement
            atacante.increasePower();
            if (atacante.getTeam() != null) {
                atacante.getTeam().increaseScore(1);
            }
        }

        if (!objetivo.isAlive()) {
            Console.logDeath(atacante, objetivo);
            //Ui Enhancement
            EventLog.get().log(atacante.getName() + " defeated " + objetivo.getName(),
                    atacante.getTeam() != null ? atacante.getTeam().getColor() : "");
            //Ui Enhancement
        }
    }

    private int calcularDano(BaseMutant atacante, BaseMutant objetivo,
            CombatDecision decisionObjetivo) {
        int dano = atacante.getPower().getDamage();
        if (decisionObjetivo == CombatDecision.ATTACK) {
            return dano;
        }

        return Math.max(1, dano / objetivo.getDefense());
    }

    private boolean sonMutantesValidos(BaseMutant firstMutant, BaseMutant secondMutant) { // Verifica si los mutantes son válidos para el combate
        if (firstMutant == null || secondMutant == null || firstMutant == secondMutant) {
            return false;
        }
        if (!firstMutant.isAlive() || !secondMutant.isAlive()) {
            return false;
        }
        if (firstMutant.getPosition() == null || secondMutant.getPosition() == null) {
            return false;
        }

        return firstMutant.getTeam() != null
                && secondMutant.getTeam() != null
                && firstMutant.getTeam() != secondMutant.getTeam();
    }
}
