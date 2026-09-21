package com.MutantBattle.game;

import com.MutantBattle.config.constants;
import com.MutantBattle.control.Position;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Mutants.Archer;
import com.MutantBattle.model.Mutants.Dragon;
import com.MutantBattle.model.Mutants.Heterodactyl;
import com.MutantBattle.model.Mutants.Monster;
import com.MutantBattle.model.Mutants.SuperHuman;
//Ui Enhancement
import java.util.ArrayList;
import java.util.List;
//Ui Enhancement
import java.util.Random;

public class BattlefieldFactory {
    private final Random random;

    public BattlefieldFactory() {
        this(new Random());
    }

    public BattlefieldFactory(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("La fuente aleatoria no puede ser nula");
        }
        this.random = random;
    }

    public Battlefield crearCampo(int teamSize) {

        Team equipoRojo = new Team("Rojo", "R", "red");
        Team equipoAzul = new Team("Azul", "A", "blue");

        //Ui Enhancement
        List<Obstacle> obstaculos = crearObstaculos();
        //Ui Enhancement

        for (int index = 1; index <= teamSize; index++) {
            //Ui Enhancement
            agregarMutante(equipoRojo, "Rojo", index, true, obstaculos);
            agregarMutante(equipoAzul, "Azul", index, false, obstaculos);
            //Ui Enhancement
        }

        //Ui Enhancement
        return new Battlefield(constants.BATTLEFIELD_WIDTH, constants.BATTLEFIELD_HEIGHT,
                equipoRojo, equipoAzul, obstaculos);
        //Ui Enhancement
    }

    //Ui Enhancement
    // Esparce entre 6 y 9 obstaculos (rocas, arboles y montanas) sin solaparse entre si,
    // dejando libres las franjas de spawn de los extremos.
    private List<Obstacle> crearObstaculos() {
        List<Obstacle> obstaculos = new ArrayList<>();
        int cantidad = 6 + random.nextInt(4);
        int margen = constants.BATTLEFIELD_WIDTH / 6;
        int intentos = 0;
        while (obstaculos.size() < cantidad && intentos < cantidad * 12) {
            intentos++;
            Obstacle.Type tipo = Obstacle.Type.values()[random.nextInt(Obstacle.Type.values().length)];
            int radio = radioPara(tipo);
            int x = margen + random.nextInt(Math.max(1, constants.BATTLEFIELD_WIDTH - 2 * margen));
            int y = radio + random.nextInt(Math.max(1, constants.BATTLEFIELD_HEIGHT - 2 * radio));
            Obstacle candidato = new Obstacle(x, y, radio, tipo);
            if (!seSolapa(candidato, obstaculos)) {
                obstaculos.add(candidato);
            }
        }
        return obstaculos;
    }

    private int radioPara(Obstacle.Type tipo) {
        switch (tipo) {
            case MOUNTAIN:
                return 45;
            case ROCK:
                return 24;
            default:
                return 20;
        }
    }

    private boolean seSolapa(Obstacle candidato, List<Obstacle> existentes) {
        for (Obstacle otro : existentes) {
            int dx = candidato.getX() - otro.getX();
            int dy = candidato.getY() - otro.getY();
            double distancia = Math.sqrt((double) dx * dx + (double) dy * dy);
            if (distancia < candidato.getRadius() + otro.getRadius() + 10) {
                return true;
            }
        }
        return false;
    }
    //Ui Enhancement

    private void agregarMutante(Team team, String teamName, int number, boolean ladoIzquierdo,
            //Ui Enhancement
            List<Obstacle> obstaculos) {
            //Ui Enhancement
        //Ui Enhancement
        BaseMutant mutant = crearMutante(teamName, number);
        //Ui Enhancement
        mutant.setDefense(constants.MIN_DEFENSE_CAPACITY + random.nextInt(constants.MAX_DEFENSE_CAPACITY));
        //Ui Enhancement
        mutant.setPosition(crearPosicionEnLado(ladoIzquierdo, obstaculos));
        //Ui Enhancement
        team.addMutant(mutant);
    }

    // Reparte cada equipo en su mitad del campo, dejando un espacio central >= ENCOUNTER_RADIUS para evitar encuentros al iniciar
    private Position crearPosicionEnLado(boolean ladoIzquierdo, List<Obstacle> obstaculos) {
        int mitad = constants.BATTLEFIELD_WIDTH / 2;
        int margen = constants.ENCOUNTER_RADIUS;
        int anchoZona = mitad - margen;
        //Ui Enhancement
        // Reintenta hasta encontrar un punto libre de obstaculos para no nacer atrapado.
        for (int intento = 0; intento < 40; intento++) {
            int x = ladoIzquierdo
                    ? random.nextInt(anchoZona)
                    : mitad + margen + random.nextInt(anchoZona);
            int y = random.nextInt(constants.BATTLEFIELD_HEIGHT);
            if (!enObstaculo(x, y, obstaculos)) {
                return new Position(x, y);
            }
        }
        int x = ladoIzquierdo
                ? random.nextInt(anchoZona)
                : mitad + margen + random.nextInt(anchoZona);
        int y = random.nextInt(constants.BATTLEFIELD_HEIGHT);
        return new Position(x, y);
        //Ui Enhancement
    }

    //Ui Enhancement
    private boolean enObstaculo(int x, int y, List<Obstacle> obstaculos) {
        for (Obstacle obstacle : obstaculos) {
            if (obstacle.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    //Ui Enhancement

    private BaseMutant crearMutante(String teamName, int number) {
        //Ui Enhancement
        int tipoMutante = random.nextInt(5);
        if (tipoMutante == 0) {
            return new Archer("Archer " + teamName + " " + number);
        }
        if (tipoMutante == 1) {
            return new Dragon("Dragon " + teamName + " " + number);
        }
        if (tipoMutante == 2) {
            return new Heterodactyl("Heterodactyl " + teamName + " " + number);
        }
        if (tipoMutante == 3) {
            return new Monster("Monster " + teamName + " " + number);
        }
        return new SuperHuman("SuperHuman " + teamName + " " + number);
        //Ui Enhancement
    }
}
