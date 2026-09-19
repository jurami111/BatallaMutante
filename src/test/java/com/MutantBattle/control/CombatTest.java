package com.MutantBattle.control;
// la funcion de este archivo es veficar quue las reclas de combate funciones correctamente 

import java.util.Random;

import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Attacks.BowShot;
import com.MutantBattle.control.Position;
public class CombatTest { // Clase de prueba para la funcionalidad de combate
    public static void main(String[] args) {
        probarDanoNormal();// Prueba de daño normal
        probarDanoContraDefensa();// Prueba de daño contra defensa
        probarAumentoMaximoDelPoder();// Prueba de aumento máximo del poder
        System.out.println("Todas las pruebas de combate pasaron correctamente.");
    }

    private static void probarDanoNormal() {// Prueba de daño normal
        BaseMutant atacante = crearMutante("Atacante", "Rojo", 100);
        BaseMutant objetivo = crearMutante("Objetivo", "Azul", 110);
        Combat combate = new Combat(25, new DecisionesControladas(true, true));

        combate.resolve(atacante, objetivo);

        verificar(objetivo.getEnergy() == 97,
                "El dano normal debe reducir 3 puntos de energia");
    }

    private static void probarDanoContraDefensa() {// Prueba de daño contra defensa
        BaseMutant atacante = crearMutante("Atacante", "Rojo", 100);
        BaseMutant objetivo = crearMutante("Objetivo", "Azul", 110);
        objetivo.setDefense(3);
        Combat combate = new Combat(25, new DecisionesControladas(true, false));

        combate.resolve(atacante, objetivo);

        verificar(objetivo.getEnergy() == 99,
                "La defensa 3 debe reducir el dano 3 a 1");
    }

    private static void probarAumentoMaximoDelPoder() {// Prueba de aumento máximo del poder                    
        BaseMutant atacante = crearMutante("Atacante", "Rojo", 100);
        BaseMutant objetivo = crearMutante("Objetivo", "Azul", 110);
        Combat combate = new Combat(25, new DecisionesControladas(true, false));

        for (int intento = 0; intento < 10; intento++) {
            combate.resolve(atacante, objetivo);
        }

        verificar(atacante.getPower().getLevel() == 7,
                "El nivel del poder no debe superar 7");
    }

    private static BaseMutant crearMutante(String nombre, String nombreEquipo, int x) {// Crea un mutante con el nombre, equipo y posición especificados
        Team equipo = new Team(nombreEquipo, nombreEquipo.substring(0, 1), "color");
        BaseMutant mutante = new BaseMutant(nombre);
        mutante.setPower(new BowShot(3, 1, 0));
        mutante.setPosition(new Position(x, 100));
        equipo.addMutant(mutante);
        return mutante;
    }

    private static void verificar(boolean condicion, String mensaje) {// Verifica que se cumpla una condición y lanza un error si no se cumple
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static class DecisionesControladas extends Random {// Clase que permite controlar las decisiones aleatorias para las pruebas
        private final boolean[] decisiones;
        private int indice;

        DecisionesControladas(boolean... decisiones) {// Constructor que inicializa las decisiones controladas
            this.decisiones = decisiones;
        }

        @Override
        public boolean nextBoolean() { // Devuelve la siguiente decisión controlada
            return decisiones[indice++ % decisiones.length];
        }
    }
}
