package com.MutantBattle.control;

import com.MutantBattle.config.constants;
import java.util.Random;

public class Movement { // Clase que representa el movimiento de un mutante dentro del campo de batalla
    private final Position position;
    private final int speed;
    private final Random random;

    public Movement(Position position, int speed) { // Constructor que utiliza una fuente aleatoria por defecto
        this(position, speed, new Random());
    }

    public Movement(Position position, int speed, Random random) { // Constructor que permite especificar la fuente aleatoria
        if (position == null) {
            throw new IllegalArgumentException("La posicion no puede ser nula");
        }
        if (speed <= 0) { // Verifica que la velocidad sea positiva
            throw new IllegalArgumentException("La velocidad debe ser positiva");
        }
        if (random == null) {
            throw new IllegalArgumentException("La fuente aleatoria no puede ser nula");
        }

        this.position = position;
        this.speed = speed;
        this.random = random;
    }

    public Position getPosition() { // Devuelve la posición actual del mutante
        return position;
    }

    public int getSpeed() { // Devuelve la velocidad del mutante
        return speed;
    }

    public void moveWithin(int width, int height) { // Mueve al mutante dentro de los límites especificados
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("El ancho y el alto deben ser positivos");
        }

        int direccionX = random.nextInt(3) - 1;
        int direccionY = random.nextInt(3) - 1;
        int nuevaX = limitar(position.getX() + direccionX * speed, width);
        int nuevaY = limitar(position.getY() + direccionY * speed, height);

        position.setX(nuevaX);
        position.setY(nuevaY);
    }

    // Se acerca un paso hacia (targetX, targetY); ocasionalmente da un paso aleatorio para no ser 100% deterministico
    public void moveTowards(int targetX, int targetY, int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("El ancho y el alto deben ser positivos");
        }

        int direccionX = Integer.compare(targetX, position.getX());
        int direccionY = Integer.compare(targetY, position.getY());
        if (random.nextInt(100) < constants.MOVEMENT_JITTER_PERCENT) {
            direccionX = random.nextInt(3) - 1;
            direccionY = random.nextInt(3) - 1;
        }

        int nuevaX = limitar(position.getX() + direccionX * speed, width);
        int nuevaY = limitar(position.getY() + direccionY * speed, height);

        position.setX(nuevaX);
        position.setY(nuevaY);
    }

    private int limitar(int coordenada, int limite) { // Limita la coordenada dentro del rango [0, limite - 1]
        return Math.max(0, Math.min(limite - 1, coordenada)); // Limita la coordenada dentro del rango [0, limite - 1]
    }
}
