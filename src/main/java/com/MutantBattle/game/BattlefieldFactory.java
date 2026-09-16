package com.MutantBattle.game;

import com.MutantBattle.config.constants;
import com.MutantBattle.control.Position;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Mutants.Archer;
import com.MutantBattle.model.Mutants.Dragon;
import com.MutantBattle.model.Mutants.Heterodactyl;
import com.MutantBattle.model.Mutants.Monster;
import com.MutantBattle.model.Mutants.SuperHuman;
import java.util.Random;

public class BattlefieldFactory {
    private final Random random;

    public BattlefieldFactory() {
        this(new Random());
    }

    public BattlefieldFactory(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("La fuente aleatoria no puede ser nula");
        }
        this.random = random;
    }

    public Battlefield crearCampo(int teamSize) {
        validarTamano(teamSize);

        Team equipoRojo = new Team("Rojo", "R", "red");
        Team equipoAzul = new Team("Azul", "A", "blue");

        for (int index = 1; index <= teamSize; index++) {
            agregarMutante(equipoRojo, "Rojo", index);
            agregarMutante(equipoAzul, "Azul", index);
        }

        return new Battlefield(
                constants.BATTLEFIELD_WIDTH,
                constants.BATTLEFIELD_HEIGHT,
                equipoRojo,
                equipoAzul);
    }

    private void agregarMutante(Team team, String teamName, int number) {
        BaseMutant mutant = crearMutante(teamName + " " + number);
        mutant.setDefense(constants.MIN_DEFENSE_CAPACITY
                + random.nextInt(constants.MAX_DEFENSE_CAPACITY));
        mutant.setPosition(new Position(
                random.nextInt(constants.BATTLEFIELD_WIDTH),
                random.nextInt(constants.BATTLEFIELD_HEIGHT)));
        team.addMutant(mutant);
    }

    private BaseMutant crearMutante(String name) {
        int tipoMutante = random.nextInt(5);
        if (tipoMutante == 0) {
            return new Archer(name);
        }
        if (tipoMutante == 1) {
            return new Dragon(name);
        }
        if (tipoMutante == 2) {
            return new Heterodactyl(name);
        }
        if (tipoMutante == 3) {
            return new Monster(name);
        }
        return new SuperHuman(name);
    }

    private void validarTamano(int teamSize) {
        if (teamSize < constants.MIN_TEAM_SIZE || teamSize > constants.MAX_TEAM_SIZE) {
            throw new IllegalArgumentException("El tamano debe estar entre 3 y 11");
        }
    }
}
