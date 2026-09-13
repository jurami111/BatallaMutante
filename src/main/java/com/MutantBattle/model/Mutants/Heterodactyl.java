package com.MutantBattle.model.Mutants;
import java.util.concurrent.ThreadLocalRandom;

import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.FlameThrower;
import com.MutantBattle.model.Attacks.WindBarrier;

public class Heterodactyl extends BaseMutant {

    public Heterodactyl(String name) {
        super(name);
        if (ThreadLocalRandom.current().nextBoolean()) {
            setPower(new WindBarrier(3, 1, 10));
        } else {
            setPower(new FlameThrower(3, 1, 5));
        }
    }
}