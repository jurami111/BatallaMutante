package com.MutantBattle.model;

public interface IPower {
    int getDamage();

    int getLevel();

    int getEnergyCost();

    default String getAll() {
        return "damage=" + getDamage()
                + ", level=" + getLevel()
                + ", energyCost=" + getEnergyCost();
    }
}