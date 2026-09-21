package com.MutantBattle.game; // Paquete que contiene la clase Team y otras relacionadas con el juego

import com.MutantBattle.model.BaseMutant; // Importa la clase BaseMutant del paquete model
import java.util.ArrayList;// Importa la clase ArrayList del paquete java.util(para almacenar la lista de mutantes)
import java.util.Collections;// Importa la clase Collections del paquete java.util (para obtener listas inmodificables)
import java.util.List;// Importa la interfaz List del paquete java.util (para manejar listas de mutantes)

public class Team { // Representa un equipo en el juego, con nombre, símbolo, color, puntuación y lista de mutantes
    // el final espara que los atributos no puedan ser modificados 
    private final String name;
    // volatile: garantiza que los hilos de combate vean el ultimo marcador
    private volatile int score;
    private final String symbol;
    private final List<BaseMutant> mutants;
    private final String color;

    public Team(String name, String symbol, String color) {// Constructor de la clase Team que inicializa el nombre, símbolo y color del equipo, y crea la lista de mutantes
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.mutants = new ArrayList<>();
    }

    public String getName() { // Devuelve el nombre del equipo
        return name;
    }

    public int getScore() { // Devuelve la puntuación del equipo
        return score;
    }

    public String getSymbol() { // Devuelve el símbolo del equipo
        return symbol;
    }

    public String getColor() { // Devuelve el color del equipo
        return color;
    }

    public void addMutant(BaseMutant mutant) { // Agrega un mutante al equipo, verificando que no sea nulo ni duplicado
        if (mutant == null) {
            throw new IllegalArgumentException("El mutante no puede ser nulo");
        }
        if (mutants.contains(mutant)) {
            throw new IllegalArgumentException("El mutante ya pertenece a este equipo");
        }

        mutants.add(mutant);
        mutant.setTeam(this);
    }

    public List<BaseMutant> getMutants() { // Devuelve la lista de mutantes del equipo (inmodificable)
        return Collections.unmodifiableList(mutants);
    }

    // synchronized: evita que dos hilos de combate incrementen el marcador al mismo tiempo
    public synchronized void increaseScore(int points) { // Incrementa la puntuación del equipo en la cantidad de puntos especificada
        if (points < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }
        score += points;
    }

    public int getAliveMutantsCount() { // Devuelve el número de mutantes vivos en el equipo
        int count = 0;
        for (BaseMutant mutant : mutants) {
            if (mutant.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public int getDeadMutantsCount() { // Devuelve el número de mutantes muertos en el equipo
        return mutants.size() - getAliveMutantsCount();
    }

    public boolean hasAliveMutants() { // Devuelve true si el equipo tiene al menos un mutante vivo
        return getAliveMutantsCount() > 0;
    }

    public boolean isDefeated() { // Devuelve true si el equipo está derrotado (tiene mutantes pero ninguno está vivo)
        return !mutants.isEmpty() && !hasAliveMutants();
    }
}
