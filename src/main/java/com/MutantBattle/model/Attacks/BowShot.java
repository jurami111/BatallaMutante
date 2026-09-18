package com.MutantBattle.model.Attacks;
import com.MutantBattle.config.constants;
import com.MutantBattle.model.IPower;

public class BowShot implements IPower { // Clase que representa el ataque de disparo con arco
    private int damage;
    private int level;
    private int energyCost;

    public BowShot(int damage, int level, int energyCost) { // Constructor que inicializa el ataque con el daño, nivel y costo de energía especificados
        this.damage = damage;
        this.level = level;
        this.energyCost = energyCost;
    }

    @Override // Indica que este método sobrescribe un método de la interfaz IPower
    public int getDamage() { // Devuelve el daño del ataque
        return damage;
    }

    @Override
    public int getLevel() { // Devuelve el nivel del ataque
        return level;
    }

    @Override
    public int getEnergyCost() { // Devuelve el costo de energía del ataque
        return energyCost;
    }

    @Override
    public void increaseLevel() { // Incrementa el nivel del ataque hasta el máximo permitido
        level = Math.min(constants.MAX_POWER_LEVEL, level + 1);
    }
}