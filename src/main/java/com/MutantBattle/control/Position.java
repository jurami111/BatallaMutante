package com.MutantBattle.control;

public class Position { //obtenemos la posicion en el plano cartesiano
    private int x;
    private int y;

    public Position(int x, int y) { // Constructor que inicializa la posición con las coordenadas x e y proporcionadas
        this.x = x;
        this.y = y;
    }

    public int getX() { // Devuelve la coordenada x de la posición
        return x;
    }

    public void setX(int x) { // Establece la coordenada x de la posición
        this.x = x;
    }

    public int getY() { // Devuelve la coordenada y de la posición
        return y;
    }

    public void setY(int y) { // Establece la coordenada y de la posición
        this.y = y;
    }

    public double distanceTo(Position other) { // Calcula la distancia euclidiana a otra posición
        if (other == null) {
            throw new IllegalArgumentException("The other position cannot be null"); // Verifica que la otra posición no sea nula
        }

        int deltaX = x - other.x;
        int deltaY = y - other.y;
        return Math.sqrt((double) deltaX * deltaX + (double) deltaY * deltaY);
    }

    public String getAll() { // Devuelve una representación en cadena de la posición con las coordenadas x e y
        return "x=" + getX() + ", y=" + getY(); // Representación en cadena de la posición
    }
}