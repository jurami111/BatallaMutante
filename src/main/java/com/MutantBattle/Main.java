package com.MutantBattle;

import java.util.Scanner;
import com.MutantBattle.config.constants;
import com.MutantBattle.control.BattleEngine;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.BattlefieldFactory;
import com.MutantBattle.game.Team;
import com.MutantBattle.ui.Console;

public class Main {

    // Corre la batalla con hilos: el motor mueve y resuelve encuentros en paralelo,
    // mientras este hilo solo consulta el estado y lo dibuja al ritmo de refresco.
    private static void runBattle(Battlefield battlefield) {
        BattleEngine engine = new BattleEngine(battlefield);
        Thread battleThread = new Thread(engine::runUntilFinished, "BattleEngine");
        battleThread.start();

        int round = 0;
        while (battleThread.isAlive()) {
            Console.printBattlefield(battlefield, round);
            try {
                Thread.sleep(constants.REFRESH_RATE_MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            round++;
        }
        Console.printBattlefield(battlefield, round);
    }

    private static void announceWinner(Battlefield battlefield) {
        Team winner = battlefield.getWinner();
        if (winner != null) {
            System.out.println("\nWinner: " + winner.getName());
        } else {
            System.out.println("\nNo winner: the battle ended in a tie.");
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