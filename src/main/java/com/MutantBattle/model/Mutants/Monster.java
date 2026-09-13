package com.MutantBattle.model.Mutants;
import java.util.concurrent.ThreadLocalRandom;

import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.DefenseBlow;
import com.MutantBattle.model.Attacks.SwordStrike;

public class Monster extends BaseMutant {

    public Monster(String name) {
        super(name);
        if (ThreadLocalRandom.current().nextBoolean()) {
            setPower(new DefenseBlow(3, 1, 10));
        } else {
            setPower(new SwordStrike(3, 1, 5));
        }
    }
}