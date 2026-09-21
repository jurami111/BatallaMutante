package com.MutantBattle.ui;

import com.MutantBattle.config.constants;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

// Panel lateral (Vista) que ocupa la altura completa: arriba muestra el roster de ambos
// equipos con su vida actual y abajo la bitacora de eventos de combate.
// Solo consulta el modelo y la bitacora compartida; no contiene logica de juego.
public class SidePanel extends JPanel {

    private static final Color PANEL_BG = new Color(20, 20, 28);
    private static final Color TEXT = new Color(20, 20, 28);
    private static final Color RED_TEAM = new Color(214, 69, 65);
    private static final Color BLUE_TEAM = new Color(72, 118, 214);
    private static final Font RETRO = new Font("Monospaced", Font.BOLD, 12);

    private final RosterView roster = new RosterView();
    private final LogView log = new LogView();

    public SidePanel() {
        setPreferredSize(new Dimension(300, 600));
        setLayout(new BorderLayout());
        setBackground(PANEL_BG);

        JScrollPane rosterScroll = new JScrollPane(roster);
        rosterScroll.setBorder(titled("TEAMS"));
        rosterScroll.getVerticalScrollBar().setUnitIncrement(16);

        JScrollPane logScroll = new JScrollPane(log);
        logScroll.setBorder(titled("EVENT LOG"));
        logScroll.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rosterScroll, logScroll);
        split.setResizeWeight(0.55);
        split.setDividerLocation(320);
        add(split, BorderLayout.CENTER);
    }

    public void setBattlefield(Battlefield battlefield) {
        roster.battlefield = battlefield;
        refresh();
    }

    // Refresca vida del roster y eventos; se llama desde el Timer de la interfaz.
    public void refresh() {
        roster.revalidate();
        roster.repaint();
        log.revalidate();
        log.repaint();
    }

    private TitledBorder titled(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 78)), title);
        border.setTitleColor(TEXT);
        border.setTitleFont(RETRO);
        return border;
    }

    private static Color colorForTeam(String teamColor) {
        if ("red".equalsIgnoreCase(teamColor)) {
            return RED_TEAM;
        }
        if ("blue".equalsIgnoreCase(teamColor)) {
            return BLUE_TEAM;
        }
        return new Color(180, 180, 190);
    }

    // Lista de mutantes de ambos equipos con su barra de vida actual.
    private static final class RosterView extends JPanel {
        private static final int ROW_HEIGHT = 26;
        private static final int HEADER_HEIGHT = 22;
        private transient Battlefield battlefield;

        RosterView() {
            setBackground(PANEL_BG);
        }

        @Override
        public Dimension getPreferredSize() {
            int mutants = 0;
            if (battlefield != null) {
                mutants = battlefield.getTeam1().getMutants().size()
                        + battlefield.getTeam2().getMutants().size();
            }
            int height = HEADER_HEIGHT * 2 + mutants * ROW_HEIGHT + 20;
            return new Dimension(280, Math.max(height, 120));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setFont(RETRO);
            if (battlefield == null) {
                g.setColor(TEXT);
                g.drawString("No battle running.", 10, 24);
                return;
            }
            int y = 6;
            y = drawTeam(g, battlefield.getTeam1(), RED_TEAM, y);
            drawTeam(g, battlefield.getTeam2(), BLUE_TEAM, y);
        }

        private int drawTeam(Graphics2D g, Team team, Color teamColor, int startY) {
            int y = startY + HEADER_HEIGHT - 6;
            g.setColor(teamColor);
            g.drawString(team.getName().toUpperCase() + "  (alive " + team.getAliveMutantsCount()
                    + "/" + team.getMutants().size() + ")", 8, y);
            y = startY + HEADER_HEIGHT;

            for (BaseMutant mutant : team.getMutants()) {
                y += ROW_HEIGHT;
                boolean alive = mutant.isAlive();
                g.setColor(alive ? TEXT : new Color(120, 120, 130));
                String name = mutant.getName();
                g.drawString(alive ? name : name + " (dead)", 12, y - 12);

                int barX = 12;
                int barY = y - 8;
                int barW = getWidth() - 24;
                int barH = 5;
                double ratio = Math.max(0.0, Math.min(1.0, mutant.getEnergy() / (double) constants.INITIAL_ENERGY));
                g.setColor(new Color(0, 0, 0, 160));
                g.fillRect(barX, barY, barW, barH);
                g.setColor(energyColor(ratio, alive));
                g.fillRect(barX, barY, (int) Math.round(barW * ratio), barH);
            }
            return y + 10;
        }

        private Color energyColor(double ratio, boolean alive) {
            if (!alive) {
                return new Color(70, 70, 80);
            }
            if (ratio > 0.6) {
                return new Color(90, 200, 90);
            }
            if (ratio > 0.3) {
                return new Color(220, 190, 60);
            }
            return new Color(210, 70, 60);
        }
    }

    // Bitacora de eventos: mas reciente arriba, coloreada segun el equipo atacante.
    private static final class LogView extends JPanel {
        private static final int LINE_HEIGHT = 18;

        LogView() {
            setBackground(PANEL_BG);
        }

        @Override
        public Dimension getPreferredSize() {
            int entries = EventLog.get().snapshot().size();
            return new Dimension(280, Math.max(entries * LINE_HEIGHT + 10, 120));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setFont(RETRO);

            List<EventLog.Entry> entries = EventLog.get().snapshot();
            if (entries.isEmpty()) {
                g.setColor(new Color(150, 150, 160));
                g.drawString("Waiting for the first clash...", 10, 22);
                return;
            }
            int y = 16;
            for (EventLog.Entry entry : entries) {
                g.setColor(colorForTeam(entry.getTeamColor()));
                g.drawString(entry.getText(), 8, y);
                y += LINE_HEIGHT;
            }
        }
    }
}
