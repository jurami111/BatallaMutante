package com.MutantBattle.game;

import com.MutantBattle.config.constants;// Importa las constantes de configuración del juego   

public class Battlefield {// Representa el campo de batalla donde se enfrentan dos equipos de mutantes
    // Constructor y métodos para gestionar el estado del campo de batalla
    private final int width;
    private final int height;
    private final Team team1;
    private final Team team2;

    public Battlefield(int width, int height, Team team1, Team team2) {//val1damos las dimensiones y los equipos
        validarDimensiones(width, height);
        validarEquipos(team1, team2);
        // Asignamos los valores validados a los atributos del campo de batalla
        this.width = width;
        this.height = height;
        this.team1 = team1;
        this.team2 = team2;
    }

    public int getWidth() { // Devuelve el ancho del campo de batalla
        return width;
    }

    public int getHeight() { // Devuelve el alto del campo de batalla
        return height;
    }

    public Team getTeam1() { // Devuelve el primer equipo
        return team1;
    }

    public Team getTeam2() { // Devuelve el segundo equipo
        return team2;
    }

    public boolean isFinished() { // Indica si la batalla ha terminado
        return team1.isDefeated() || team2.isDefeated();
    }

    public Team getWinner() { // Devuelve el equipo ganador, o null si la batalla no ha terminado o hay empate
        if (!isFinished()) {
            return null;
        }
        if (team1.isDefeated() && !team2.isDefeated()) {// Si el equipo 1 está derrotado y el equipo 2 no, el ganador es el equipo 2
            return team2;
        }
        if (team2.isDefeated() && !team1.isDefeated()) {// Si el equipo 2 está derrotado y el equipo 1 no, el ganador es el equipo 1
            return team1;
        }
        return null;
    }

    private void validarDimensiones(int width, int height) {// Valida que las dimensiones del campo de batalla sean positivas
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("El ancho y el alto deben ser positivos");
        }
    }

    private void validarEquipos(Team team1, Team team2) {// Valida que los equipos sean válidos y cumplan con las restricciones de tamaño
        if (team1 == null || team2 == null) {//
            throw new IllegalArgumentException("El campo necesita dos equipos");
        }
        if (team1 == team2) {
            throw new IllegalArgumentException("Los equipos deben ser diferentes");
        }

        int team1Size = team1.getMutants().size();// Obtenemos el tamaño del equipo 1
        int team2Size = team2.getMutants().size();// Obtenemos el tamaño del equipo 2
        if (team1Size < constants.MIN_TEAM_SIZE || team1Size > constants.MAX_TEAM_SIZE) {// Validamos el tamaño del equipo 1
            throw new IllegalArgumentException("El tamano del equipo 1 debe estar entre 3 y 11");
        }
        if (team2Size < constants.MIN_TEAM_SIZE || team2Size > constants.MAX_TEAM_SIZE) {// Validamos el tamaño del equipo 2
            throw new IllegalArgumentException("El tamano del equipo 2 debe estar entre 3 y 11");
        }
        if (team2Size != team1Size) {// Validamos que ambos equipos tengan el mismo tamaño
            throw new IllegalArgumentException("Los equipos deben tener el mismo tamano");
        }
    }
}
