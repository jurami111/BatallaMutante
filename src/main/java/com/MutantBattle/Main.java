package com.MutantBattle;

import com.MutantBattle.control.Position;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.ModelMain;
import com.MutantBattle.game.GameMain;
import com.MutantBattle.control.ControlMain;
import com.MutantBattle.ui.UIMain;
import com.MutantBattle.model.Mutants.Archer;
import com.MutantBattle.model.Mutants.Dragon;
import com.MutantBattle.model.Mutants.Heterodactyl;
import com.MutantBattle.model.Mutants.Monster;
import com.MutantBattle.model.Mutants.SuperHuman;
import static com.MutantBattle.config.constants.DEMO_TEAM_SIZE;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Mutant Battle!");
        ModelMain.modelMain();
        UIMain.uiMain();
        GameMain.gameMain();
        ControlMain.controlMain();
        Team redTeam = new Team("Red", "R", "red");
        Team blueTeam = new Team("Blue", "B", "blue");

        for (int index = 0; index < DEMO_TEAM_SIZE; index++) {
            BaseMutant redMutant = createMutant(redTeam.getName(), index);
            BaseMutant blueMutant = createMutant(blueTeam.getName(), index);

            redMutant.setPosition(new Position(index * 10, index * 10));
            blueMutant.setPosition(new Position(100 - index * 10, 100 - index * 10));

            redTeam.addMutant(redMutant);
            blueTeam.addMutant(blueMutant);
        }

        printTeam(redTeam);
        printTeam(blueTeam);
    }

    private static BaseMutant createMutant(String teamName, int index) {
        String name = teamName + " mutant " + (index + 1);
        switch (index) {
            case 0:
                return new Archer(name);
            case 1:
                return new Dragon(name);
            case 2:
                return new Heterodactyl(name);
            case 3:
                return new Monster(name);
            default:
                return new SuperHuman(name);
        }
    }

    private static void printTeam(Team team) {
        System.out.println("\n" + team.getAll());
        for (BaseMutant mutant : team.getMutants()) {
            System.out.println(mutant.getAll());
        }
    }
}