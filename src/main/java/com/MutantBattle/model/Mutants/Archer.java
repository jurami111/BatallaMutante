package com.MutantBattle.model.Mutants;
import java.util.concurrent.ThreadLocalRandom;

import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.BowShot;
import com.MutantBattle.model.Attacks.DefenseBlow;

public class Archer extends BaseMutant {

    public Archer(String name) {
        super(name);
        if (ThreadLocalRandom.current().nextBoolean()) {
            setPower(new DefenseBlow(3, 1, 10));
        } else {
            setPower(new BowShot(3, 1, 5));
        }
    }
}