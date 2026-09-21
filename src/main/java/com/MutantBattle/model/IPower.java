package com.MutantBattle.model;

public interface IPower {
    int getDamage();

    int getLevel();

    int getEnergyCost();

    void increaseLevel();
}