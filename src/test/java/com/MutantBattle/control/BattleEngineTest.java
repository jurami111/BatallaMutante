package com.MutantBattle.control;

import com.MutantBattle.config.constants;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.Mutants.Archer;
import com.MutantBattle.model.Mutants.Dragon;
import com.MutantBattle.model.Mutants.Heterodactyl;
import com.MutantBattle.model.Mutants.Monster;
import com.MutantBattle.model.Mutants.SuperHuman;
import java.util.Random;

// Prueba end-to-end sin UI: corre batallas completas con equipos pequenos, medianos y grandes,
// y verifica que siempre terminan con un ganador (o un empate) coherente con los conteos finales
public class BattleEngineTest {
    private static final int CAMPO_COMPACTO = 60;
    private static final long INTERVALO_RAPIDO_MILLIS = 5;

    public static void main(String[] args) {
        probarBatalla(3);
        probarBatalla(7);
        probarBatalla(11);
        System.out.println("Todas las batallas end-to-end terminaron correctamente.");
    }

    private static void probarBatalla(int teamSize) {
        Battlefield battlefield = crearCampoCompacto(teamSize, new Random(teamSize));
        Combat combate = new Combat(constants.ENCOUNTER_RADIUS);

        long inicio = System.currentTimeMillis();
        Team ganador = new BattleEngine(battlefield, combate, INTERVALO_RAPIDO_MILLIS).runUntilFinished();
        long duracionMillis = System.currentTimeMillis() - inicio;

        verificar(battlefield.isFinished(), "La batalla de tamano " + teamSize + " debe terminar");
        boolean conteosCoherentes = battlefield.getTeam1().isDefeated() != battlefield.getTeam2().isDefeated();
        verificar(conteosCoherentes, "Exactamente un equipo debe quedar derrotado");
        verificar(ganador == battlefield.getTeam1() || ganador == battlefield.getTeam2(),
                "El ganador debe ser alguno de los dos equipos");

        System.out.println("Tamano " + teamSize + " -> ganador: " + ganador.getName()
                + ", vivos equipo1=" + battlefield.getTeam1().getAliveMutantsCount()
                + ", vivos equipo2=" + battlefield.getTeam2().getAliveMutantsCount()
                + ", duracion=" + duracionMillis + "ms");
    }

    private static Battlefield crearCampoCompacto(int teamSize, Random random) {
        Team equipoRojo = new Team("Rojo", "R", "red");
        Team equipoAzul = new Team("Azul", "A", "blue");

        for (int index = 1; index <= teamSize; index++) {
            agregarMutante(equipoRojo, "Rojo " + index, index, random);
            agregarMutante(equipoAzul, "Azul " + index, index, random);
        }

        return new Battlefield(CAMPO_COMPACTO, CAMPO_COMPACTO, equipoRojo, equipoAzul);
    }

    private static void agregarMutante(Team team, String name, int index, Random random) {
        BaseMutant mutante = crearMutante(name, index);
        mutante.setDefense(constants.MIN_DEFENSE_CAPACITY + random.nextInt(constants.MAX_DEFENSE_CAPACITY));
        mutante.setPosition(new Position(random.nextInt(CAMPO_COMPACTO), random.nextInt(CAMPO_COMPACTO)));
        team.addMutant(mutante);
    }

    private static BaseMutant crearMutante(String name, int index) {
        switch (index % 5) {
            case 0:
                return new Archer(name);
            case 1:
                return new Dragon(name);
            case 2:
                return new Heterodactyl(name);
            case 3:
                return new Monster(name);
            default:
                return new SuperHuman(name);
        }
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
