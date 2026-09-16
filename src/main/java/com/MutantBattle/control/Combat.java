package com.MutantBattle.control;

import com.MutantBattle.config.constants;
import com.MutantBattle.model.BaseMutant;

public class Combat { // Clase que representa el combate entre mutantes
    private final int detectionRadius;

    public Combat() { // Constructor que utiliza el radio de detección por defecto
        this(constants.ENCOUNTER_RADIUS);
    }

    public Combat(int detectionRadius) { // Constructor que permite especificar el radio de detección
        if (detectionRadius <= 0) {
            throw new IllegalArgumentException("El radio de deteccion debe ser positivo");
        }
        this.detectionRadius = detectionRadius;
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
