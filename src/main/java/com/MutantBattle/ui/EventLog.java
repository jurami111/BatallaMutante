package com.MutantBattle.ui;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

// Bitacora de eventos compartida y segura para hilos: los hilos de combate registran
// cada ataque/muerte y la interfaz la consulta para mostrarla en el panel lateral.
public final class EventLog {

    // Entrada de la bitacora: texto del evento y color del equipo atacante ("red"/"blue"/"").
    public static final class Entry {
        private final String text;
        private final String teamColor;

        public Entry(String text, String teamColor) {
            this.text = text;
            this.teamColor = teamColor;
        }

        public String getText() {
            return text;
        }

        public String getTeamColor() {
            return teamColor;
        }
    }

    private static final EventLog INSTANCE = new EventLog();
    private static final int MAX_ENTRIES = 200;

    private final Deque<Entry> entries = new ConcurrentLinkedDeque<>();

    private EventLog() {
    }

    public static EventLog get() {
        return INSTANCE;
    }

    public void log(String text, String teamColor) {
        entries.addLast(new Entry(text, teamColor));
        while (entries.size() > MAX_ENTRIES) {
            entries.pollFirst();
        }
    }

    // Devuelve una copia de las entradas mas recientes primero.
    public List<Entry> snapshot() {
        List<Entry> copy = new ArrayList<>(entries);
        java.util.Collections.reverse(copy);
        return copy;
    }

    public void clear() {
        entries.clear();
    }
}
