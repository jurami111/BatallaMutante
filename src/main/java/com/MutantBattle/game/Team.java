package com.MutantBattle.game; // Paquete que contiene la clase Team y otras relacionadas con el juego

import com.MutantBattle.model.BaseMutant; // Importa la clase BaseMutant del paquete model
import java.util.ArrayList;// Importa la clase ArrayList del paquete java.util(para almacenar la lista de mutantes)
import java.util.Collections;// Importa la clase Collections del paquete java.util (para obtener listas inmodificables)
import java.util.List;// Importa la interfaz List del paquete java.util (para manejar listas de mutantes)

public class Team { // Representa un equipo en el juego, con nombre, símbolo, color, puntuación y lista de mutantes
    // el final espara que los atributos no puedan ser modificados 
    private final String name;
    private int score;
    private final String symbol;
    private final List<BaseMutant> mutants;
    private final String color;

    public Team(String name, String symbol, String color) {// Constructor de la clase Team que inicializa el nombre, símbolo y color del equipo, y crea la lista de mutantes
        if (name == null || name.trim().isEmpty()) { // Verifica que el nombre no sea nulo ni vacío
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacio");
        }
        if (symbol == null || symbol.trim().isEmpty()) { // verifica que el símbolo no sea nulo ni vacío
            throw new IllegalArgumentException("El simbolo del equipo no puede estar vacio");
        }
        if (color == null || color.trim().isEmpty()) { // verifica que el color no sea nulo ni vacío
            throw new IllegalArgumentException("El color del equipo no puede estar vacio");
        }

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

    public void increaseScore(int points) { // Incrementa la puntuación del equipo en la cantidad de puntos especificada
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

    public String getAll() { // Devuelve una representación en cadena de todos los atributos del equipo 
        return "name=" + name
                + ", score=" + score
                + ", symbol=" + symbol
                + ", color=" + color
                + ", mutants=" + mutants.size()
                + ", aliveMutants=" + getAliveMutantsCount(); // Devuelve una representación en cadena de todos los atributos del equipo
    }
}
