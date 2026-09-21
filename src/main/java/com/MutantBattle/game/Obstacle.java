package com.MutantBattle.game;

// Elemento del entorno (roca, arbol o montana) que ocupa un area circular del campo
// y que los mutantes deben rodear. Es solo un obstaculo de posicion; no tiene logica de juego.
public class Obstacle {

    public enum Type {
        ROCK, TREE, MOUNTAIN
    }

    private final int x;
    private final int y;
    private final int radius;
    private final Type type;

    public Obstacle(int x, int y, int radius, Type type) {
        if (radius <= 0) {
            throw new IllegalArgumentException("El radio del obstaculo debe ser positivo");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo de obstaculo no puede ser nulo");
        }
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public Type getType() {
        return type;
    }

    // Indica si el punto (px, py) cae dentro del area del obstaculo.
    public boolean contains(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return (long) dx * dx + (long) dy * dy <= (long) radius * radius;
    }
}
