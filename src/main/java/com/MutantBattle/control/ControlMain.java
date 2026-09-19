package com.MutantBattle.control;
//Aca se va a controlar el Combate y el Movimiento

import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Mutants.Archer;
import com.MutantBattle.model.Mutants.Dragon;
import com.MutantBattle.model.Mutants.Heterodactyl;
import com.MutantBattle.model.Mutants.Monster;
import com.MutantBattle.model.Mutants.SuperHuman;

public class ControlMain {
    public static void controlMain() {
        System.out.println("Welcome to the Mutant Battle Control!");
        probarCondicionDeFinalizacion();
    }

    // Corre una batalla pequena hasta el final y verifica que se calcule un ganador
    private static void probarCondicionDeFinalizacion() {
        Team equipoRojo = new Team("Rojo", "R", "red");
        Team equipoAzul = new Team("Azul", "A", "blue");

        // Posiciones cercanas y energia baja para que la demo termine rapido
        agregarMutante(equipoRojo, new Archer("Rojo 1"), 10, 10, 5);
        agregarMutante(equipoRojo, new Dragon("Rojo 2"), 15, 15, 5);
        agregarMutante(equipoRojo, new Heterodactyl("Rojo 3"), 20, 20, 5);
        agregarMutante(equipoAzul, new Monster("Azul 1"), 12, 12, 5);
        agregarMutante(equipoAzul, new Archer("Azul 2"), 18, 18, 5);
        agregarMutante(equipoAzul, new SuperHuman("Azul 3"), 22, 22, 5);

        Battlefield battlefield = new Battlefield(100, 100, equipoRojo, equipoAzul);
        Team ganador = new BattleEngine(battlefield).runUntilFinished();

        System.out.println("Batalla finalizada. Vivos equipo 1: " + battlefield.getTeam1().getAliveMutantsCount()
                + ", vivos equipo 2: " + battlefield.getTeam2().getAliveMutantsCount());
        System.out.println("Ganador: " + (ganador == null ? "empate" : ganador.getName()));
    }

    private static void agregarMutante(Team team, BaseMutant mutante, int x, int y, int energia) {
        mutante.setPosition(new Position(x, y));
        mutante.setEnergy(energia);
        team.addMutant(mutante);
    }
}