package com.MutantBattle.model;

import com.MutantBattle.config.constants;
import com.MutantBattle.control.Position;
import com.MutantBattle.game.Team;

public class BaseMutant {
    private final String name;
    private int energy;
    private int defense;
    private IPower power;
    private Position position;
    private Team team;

    public BaseMutant(String name) {
        this.name = name;
        this.energy = constants.INITIAL_ENERGY;
        this.defense = constants.MIN_DEFENSE_CAPACITY;
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, energy);
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(constants.MIN_DEFENSE_CAPACITY,
                Math.min(constants.MAX_DEFENSE_CAPACITY, defense));
    }

    public IPower getPower() {
        return power;
    }

    public void setPower(IPower power) {
        this.power = power;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public String getAll() {
        return "name=" + name
                + ", energy=" + energy
                + ", defense=" + defense
                + ", power={" + getPowerData() + "}"
                + ", position={" + (position == null ? "none" : position.getAll()) + "}"
                + ", team={" + (team == null ? "none" : team.getAll()) + "}";
    }

    private String getPowerData() {
        if (power == null) {
            return "none";
        }
        return "type=" + power.getClass().getSimpleName() + ", " + power.getAll();
    }
}