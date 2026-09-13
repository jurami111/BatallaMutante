package com.MutantBattle.model.Mutants;
import java.util.concurrent.ThreadLocalRandom;

import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.FlameThrower;
import com.MutantBattle.model.Attacks.WindBarrier;

public class Dragon extends BaseMutant {

    public Dragon(String name) {
        super(name);
        if (ThreadLocalRandom.current().nextBoolean()) {
            setPower(new WindBarrier(3, 1, 5));
        } else {
            setPower(new FlameThrower(3, 1, 10));
        }
    }
}