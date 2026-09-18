package com.MutantBattle;

import java.util.Scanner;
import com.MutantBattle.config.constants;
import com.MutantBattle.control.Combat;
import com.MutantBattle.control.Movement;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.BattlefieldFactory;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.ui.Console;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // Tope de seguridad para el movimiento aleatorio de la integracion sin hilos (semana 1)
    private static final int MAX_ROUNDS = 500;

    private static Map<BaseMutant, Movement> createMovements(Battlefield battlefield) {
    Map<BaseMutant, Movement> movements = new HashMap<>();
    for (BaseMutant mutant : battlefield.getTeam1().getMutants()) {
        movements.put(mutant, new Movement(mutant.getPosition(), constants.MUTANT_SPEED));
    }
    for (BaseMutant mutant : battlefield.getTeam2().getMutants()) {
        movements.put(mutant, new Movement(mutant.getPosition(), constants.MUTANT_SPEED));
    }
    return movements;
    }

    private static void moveTeam(Team team, Team opponent, Map<BaseMutant, Movement> movements, int width, int height) {
        for (BaseMutant mutant : team.getMutants()) {
            if (!mutant.isAlive()) {
                continue;
            }
            Movement movement = movements.get(mutant);
            BaseMutant nearestEnemy = findNearestEnemy(mutant, opponent);
            if (nearestEnemy != null) {
                movement.moveTowards(nearestEnemy.getPosition().getX(), nearestEnemy.getPosition().getY(), width, height);
            } else {
                movement.moveWithin(width, height);
            }
        }
    }

    // Busca el mutante vivo mas cercano del equipo rival, para que el movimiento se dirija hacia el
    private static BaseMutant findNearestEnemy(BaseMutant mutant, Team opponent) {
        BaseMutant nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (BaseMutant candidate : opponent.getMutants()) {
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

        private static void resolveEncounters(Team team1, Team team2, Combat combat) {
        for (BaseMutant firstMutant : team1.getMutants()) {
            for (BaseMutant secondMutant : team2.getMutants()) {
                combat.resolve(firstMutant, secondMutant);
            }
        }
    }

    private static void announceWinner(Battlefield battlefield) {
        Team winner = battlefield.getWinner();
        if (winner != null) {
            System.out.println("\nWinner: " + winner.getName());
        } else {
            System.out.println("\nNo winner yet after " + MAX_ROUNDS + " rounds.");
        }
    }

        private static void runBattle(Battlefield battlefield) {
        Combat combat = new Combat();
        Map<BaseMutant, Movement> movements = createMovements(battlefield);

        int round = 0;
        while (!battlefield.isFinished() && round < MAX_ROUNDS) {
            moveTeam(battlefield.getTeam1(), battlefield.getTeam2(), movements, battlefield.getWidth(), battlefield.getHeight());
            moveTeam(battlefield.getTeam2(), battlefield.getTeam1(), movements, battlefield.getWidth(), battlefield.getHeight());
            resolveEncounters(battlefield.getTeam1(), battlefield.getTeam2(), combat);
            Console.printBattlefield(battlefield, round);
            try {
                Thread.sleep(constants.REFRESH_RATE_MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            round++;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int Integrantes;
        do {
            System.out.println("Enter the number of integrants (minimum " + constants.MIN_TEAM_SIZE + "): ");
            Integrantes = scanner.nextInt();
        } while ( Integrantes < constants.MIN_TEAM_SIZE || Integrantes > constants.MAX_TEAM_SIZE);

        Battlefield battlefield = new BattlefieldFactory().crearCampo(Integrantes);
        runBattle(battlefield);
        announceWinner(battlefield);
        scanner.close();
    }


}