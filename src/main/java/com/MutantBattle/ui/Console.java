package com.MutantBattle.ui;

import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import java.util.Arrays;

public class Console {
    // ANSI color codes
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    private static final int CONSOLE_WIDTH = 60;
    private static final int CONSOLE_HEIGHT = 25;

    public static void printBattlefield(Battlefield battlefield, int round) {
        char[][] grid = new char[CONSOLE_HEIGHT][CONSOLE_WIDTH];
        for (char[] row : grid) {
            Arrays.fill(row, '.');
        }

        placeTeam(battlefield.getTeam1(), battlefield, grid, false);
        placeTeam(battlefield.getTeam2(), battlefield, grid, false);

        System.out.println("\n--- Round " + round + " ---");
        for (char[] row : grid) {
            System.out.println(renderRowWithColor(new String(row), battlefield));
        }
    }

    // Renderiza una fila de la grilla con colores ANSI segun el simbolo del equipo
    private static String renderRowWithColor(String row, Battlefield battlefield) {
        StringBuilder colored = new StringBuilder();
        for (char c : row.toCharArray()) {
            if (c == battlefield.getTeam1().getSymbol().charAt(0)) {
                colored.append(RED).append(c).append(RESET);
            } else if (c == battlefield.getTeam2().getSymbol().charAt(0)) {
                colored.append(BLUE).append(c).append(RESET);
            } else {
                colored.append(c);
            }
        }
        return colored.toString();
    }

    // Coloca cada mutante vivo en su posicion escalada en la grilla, marcado en verde si esta en combate cercano
    private static void placeTeam(Team team, Battlefield battlefield, char[][] grid, boolean inCombat) {
        for (BaseMutant mutant : team.getMutants()) {
            if (!mutant.isAlive()) {
                continue;
            }
            int col = mutant.getPosition().getX() * CONSOLE_WIDTH / battlefield.getWidth();
            int row = mutant.getPosition().getY() * CONSOLE_HEIGHT / battlefield.getHeight();
            col = Math.min(CONSOLE_WIDTH - 1, Math.max(0, col));
            row = Math.min(CONSOLE_HEIGHT - 1, Math.max(0, row));
            grid[row][col] = team.getSymbol().charAt(0);
        }
    }

    // Registra un encuentro de combate con colores y dano aplicado
    public static void logEncounter(BaseMutant attacker, BaseMutant defender, int damage) {
        String attackerTeam = attacker.getTeam() != null ? attacker.getTeam().getName() : "sin equipo";
        String defenderTeam = defender.getTeam() != null ? defender.getTeam().getName() : "sin equipo";
        String symbol = attacker.getTeam().getSymbol();
        String color = symbol.equals("R") ? RED : BLUE;

        System.out.println(color + symbol + RESET + " " + attacker.getName() + " attacked " +
                defender.getTeam().getSymbol() + " " + defender.getName() +
                " -> " + GREEN + damage + " damage" + RESET);
    }

    // Registra la muerte de un mutante en verde
    public static void logDeath(BaseMutant attacker, BaseMutant defender) {
        String attackerSymbol = attacker.getTeam().getSymbol();
        String defenderSymbol = defender.getTeam().getSymbol();
        String attackerColor = attackerSymbol.equals("R") ? RED : BLUE;
        String defenderColor = defenderSymbol.equals("R") ? RED : BLUE;

        System.out.println(attackerColor + attackerSymbol + RESET + " " + attacker.getName() +
                " killed " + defenderColor + defenderSymbol + RESET + " " + defender.getName());
    }

    // Imprime el mapa de movimientos inicial
    public static void printMovements(java.util.Map<BaseMutant, java.util.function.Function<?, ?>> movements) {
        // This is used for debugging; typically disabled for normal play
    }
}
