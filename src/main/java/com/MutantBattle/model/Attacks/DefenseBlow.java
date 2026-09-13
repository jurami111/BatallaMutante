package com.MutantBattle.model.Attacks;
import com.MutantBattle.model.IPower;

public class DefenseBlow implements IPower {
    private int damage;
    private int level;
    private int energyCost;

    public DefenseBlow(int damage, int level, int energyCost) {
        this.damage = damage;
        this.level = level;
        this.energyCost = energyCost;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnergyCost() {
        return energyCost;
    }
}