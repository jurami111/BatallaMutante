package com.MutantBattle.model.Mutants;
import java.util.concurrent.ThreadLocalRandom;

import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.BowShot;
import com.MutantBattle.model.Attacks.SwordStrike;

public class SuperHuman extends BaseMutant {

    public SuperHuman(String name) {
        super(name);
        if (ThreadLocalRandom.current().nextBoolean()) {
            setPower(new SwordStrike(3, 1, 7));
        } else {
            setPower(new BowShot(3, 1, 7));
        }
    }
}