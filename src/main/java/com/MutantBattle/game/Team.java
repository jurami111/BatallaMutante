package com.MutantBattle.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.MutantBattle.model.BaseMutant;

public class Team {
    private String name;
    private int score;
    private String symbol;
    private final List<BaseMutant> mutants;
    private String color;

    public Team(String name, String symbol, String color) {
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.mutants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public void addMutant(BaseMutant mutant) {
        mutants.add(mutant);
        mutant.setTeam(this);
    }

    public List<BaseMutant> getMutants() {
        return Collections.unmodifiableList(mutants);
    }

    public void increaseScore(int points) {
        score += points;
    }

    public int getAliveMutantsCount() {
        int count = 0;
        for (BaseMutant mutant : mutants) {
            if (mutant.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public String getAll() {
        return "name=" + name
                + ", score=" + score
                + ", symbol=" + symbol
                + ", color=" + color
                + ", mutants=" + mutants.size()
                + ", aliveMutants=" + getAliveMutantsCount();
    }
}
