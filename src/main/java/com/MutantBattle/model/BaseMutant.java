package com.MutantBattle.model;

import com.MutantBattle.config.constants;
import com.MutantBattle.control.Position;
import com.MutantBattle.game.Team;
import java.util.concurrent.atomic.AtomicLong;

public class BaseMutant {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final long id;
    private final String name;
    // volatile: garantiza que los hilos de combate vean el ultimo valor de energia
    private volatile int energy;
    private int defense;
    private IPower power;
    private Position position;
    private Team team;

    public BaseMutant(String name) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.name = name;
        this.energy = constants.INITIAL_ENERGY;
        this.defense = constants.MIN_DEFENSE_CAPACITY;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public synchronized void setEnergy(int energy) {
        this.energy = Math.max(0, energy);
    }

    // synchronized: evita que dos combates concurrentes pisen el mismo cambio de energia
    public synchronized void receiveDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("El dano no puede ser negativo");
        }
        setEnergy(energy - damage);
    }

    public synchronized void increasePower() {
        if (power != null) {
            power.increaseLevel();
        }
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