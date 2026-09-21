package com.MutantBattle.ui;

import com.MutantBattle.config.constants;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.Obstacle;
import com.MutantBattle.game.Team;
import com.MutantBattle.model.BaseMutant;
import com.MutantBattle.model.IPower;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

// Lienzo estilo pixel-art que dibuja el campo de batalla en tiempo real.
// Consulta el modelo (no contiene logica de juego) y muestra un tooltip al pasar
// el cursor sobre un mutante con su Nombre, Poder, Energia y Equipo.
public class BattlefieldPanel extends JPanel {

    private static final int PIXEL = 3;                       // tamano de cada pixel del sprite
    private static final int SPRITE_PX = PixelSprites.GRID * PIXEL; // ancho/alto del sprite en pantalla
    private static final int TILE = 24;                       // tamano del mosaico de fondo

    private static final Color GRASS_A = new Color(46, 68, 42);
    private static final Color GRASS_B = new Color(40, 60, 37);
    private static final Color RED_TEAM = new Color(214, 69, 65);
    private static final Color BLUE_TEAM = new Color(72, 118, 214);
    private static final Color TOOLTIP_BG = new Color(18, 18, 24, 235);
    private static final Color TOOLTIP_BORDER = new Color(235, 235, 245);

    private final Font pixelFont = new Font("Monospaced", Font.BOLD, 12);

    private transient Battlefield battlefield;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private int mouseX = -1;
    private int mouseY = -1;

    public BattlefieldPanel() {
        setBackground(GRASS_B);
        MouseAdapter tracker = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseX = -1;
                mouseY = -1;
                repaint();
            }
        };
        addMouseMotionListener(tracker);
        addMouseListener(tracker);
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawGrass(g);

        if (battlefield == null) {
            drawCenteredMessage(g, "PRESS  START  TO  BEGIN");
            return;
        }

        scaleX = (double) getWidth() / battlefield.getWidth();
        scaleY = (double) getHeight() / battlefield.getHeight();

        drawObstacles(g);

        BaseMutant hovered = null;
        hovered = drawTeam(g, battlefield.getTeam1(), RED_TEAM, hovered);
        hovered = drawTeam(g, battlefield.getTeam2(), BLUE_TEAM, hovered);

        if (hovered != null) {
            drawTooltip(g, hovered);
        }
    }

    // Dibuja el fondo con un patron de mosaicos alternos (estetica retro).
    private void drawGrass(Graphics2D g) {
        for (int y = 0; y < getHeight(); y += TILE) {
            for (int x = 0; x < getWidth(); x += TILE) {
                boolean even = ((x / TILE) + (y / TILE)) % 2 == 0;
                g.setColor(even ? GRASS_A : GRASS_B);
                g.fillRect(x, y, TILE, TILE);
            }
        }
    }

    // Dibuja obstaculos del entorno con estetica pixelada, escalados a la posicion del campo.
    private void drawObstacles(Graphics2D g) {
        for (Obstacle obstacle : battlefield.getObstacles()) {
            int cx = (int) Math.round(obstacle.getX() * scaleX);
            int cy = (int) Math.round(obstacle.getY() * scaleY);
            int r = (int) Math.round(obstacle.getRadius() * Math.min(scaleX, scaleY));
            switch (obstacle.getType()) {
                case MOUNTAIN:
                    drawMountain(g, cx, cy, r);
                    break;
                case TREE:
                    drawTree(g, cx, cy, r);
                    break;
                default:
                    drawRock(g, cx, cy, r);
                    break;
            }
        }
    }

    private void drawRock(Graphics2D g, int cx, int cy, int r) {
        g.setColor(new Color(70, 70, 80));
        g.fillRect(cx - r, cy - r / 2, r * 2, (int) (r * 1.5));
        g.setColor(new Color(110, 110, 122));
        g.fillRect(cx - r, cy - r / 2, r * 2, r);
        g.setColor(new Color(150, 150, 162));
        g.fillRect(cx - r / 2, cy - r / 2, r, r / 2);
    }

    private void drawTree(Graphics2D g, int cx, int cy, int r) {
        g.setColor(new Color(92, 60, 35));
        g.fillRect(cx - PIXEL, cy, PIXEL * 2, r);
        g.setColor(new Color(40, 110, 52));
        g.fillRect(cx - r, cy - r, r * 2, r + r / 2);
        g.setColor(new Color(64, 156, 74));
        g.fillRect(cx - r / 2, cy - r, r, r);
    }

    private void drawMountain(Graphics2D g, int cx, int cy, int r) {
        g.setColor(new Color(80, 78, 90));
        int[] xs = {cx - r, cx, cx + r};
        int[] ys = {cy + r, cy - r, cy + r};
        g.fillPolygon(xs, ys, 3);
        g.setColor(new Color(112, 108, 122));
        int[] xs2 = {cx - r / 2, cx, cx + r / 3};
        int[] ys2 = {cy + r, cy - r, cy + r};
        g.fillPolygon(xs2, ys2, 3);
        g.setColor(new Color(232, 232, 240));
        int[] xsCap = {cx - r / 4, cx, cx + r / 4};
        int[] ysCap = {cy - r / 2, cy - r, cy - r / 2};
        g.fillPolygon(xsCap, ysCap, 3);
    }

    private BaseMutant drawTeam(Graphics2D g, Team team, Color teamColor, BaseMutant hovered) {
        BaseMutant result = hovered;
        for (BaseMutant mutant : team.getMutants()) {
            if (mutant.getPosition() == null) {
                continue;
            }
            Rectangle box = spriteBox(mutant);
            if (!mutant.isAlive()) {
                drawTombstone(g, box);
                continue;
            }
            drawEnergyBar(g, mutant, box);
            PixelSprites.draw(g, PixelSprites.spriteFor(mutant), box.x, box.y, PIXEL,
                    teamColor, PixelSprites.accentFor(mutant));
            if (box.contains(mouseX, mouseY)) {
                result = mutant; // el ultimo dibujado gana el hover
            }
        }
        return result;
    }

    private Rectangle spriteBox(BaseMutant mutant) {
        int centerX = (int) Math.round(mutant.getPosition().getX() * scaleX);
        int centerY = (int) Math.round(mutant.getPosition().getY() * scaleY);
        int topLeftX = centerX - SPRITE_PX / 2;
        int topLeftY = centerY - SPRITE_PX / 2;
        return new Rectangle(topLeftX, topLeftY, SPRITE_PX, SPRITE_PX);
    }

    private void drawEnergyBar(Graphics2D g, BaseMutant mutant, Rectangle box) {
        int barHeight = 4;
        int barY = box.y - barHeight - 2;
        double ratio = Math.max(0.0, Math.min(1.0, mutant.getEnergy() / (double) constants.INITIAL_ENERGY));
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(box.x, barY, SPRITE_PX, barHeight);
        g.setColor(energyColor(ratio));
        g.fillRect(box.x, barY, (int) Math.round(SPRITE_PX * ratio), barHeight);
    }

    private Color energyColor(double ratio) {
        if (ratio > 0.6) {
            return new Color(90, 200, 90);
        }
        if (ratio > 0.3) {
            return new Color(220, 190, 60);
        }
        return new Color(210, 70, 60);
    }

    private void drawTombstone(Graphics2D g, Rectangle box) {
        g.setColor(new Color(90, 90, 100));
        int w = SPRITE_PX - 6;
        int h = SPRITE_PX - 4;
        g.fillRect(box.x + 3, box.y + 6, w, h - 6);
        g.fillArc(box.x + 3, box.y + 2, w, w, 0, 180);
        g.setColor(new Color(40, 40, 48));
        g.setFont(pixelFont);
        g.drawString("+", box.x + SPRITE_PX / 2 - 3, box.y + SPRITE_PX / 2 + 4);
    }

    // Cuadro de informacion con Nombre, Poder, Energia y Equipo del mutante bajo el cursor.
    private void drawTooltip(Graphics2D g, BaseMutant mutant) {
        IPower power = mutant.getPower();
        String powerText = power == null
                ? "None"
                : power.getClass().getSimpleName() + " Lv" + power.getLevel() + " (dmg " + power.getDamage() + ")";
        String teamName = mutant.getTeam() != null ? mutant.getTeam().getName() : "-";
        String[] lines = {
                "Name:   " + mutant.getName(),
                "Type:   " + mutant.getClass().getSimpleName(),
                "Power:  " + powerText,
                "Energy: " + mutant.getEnergy() + " / " + constants.INITIAL_ENERGY,
                "Team:   " + teamName
        };

        g.setFont(pixelFont);
        int lineHeight = g.getFontMetrics().getHeight();
        int textWidth = 0;
        for (String line : lines) {
            textWidth = Math.max(textWidth, g.getFontMetrics().stringWidth(line));
        }
        int padding = 8;
        int boxWidth = textWidth + padding * 2;
        int boxHeight = lines.length * lineHeight + padding * 2;

        int boxX = mouseX + 14;
        int boxY = mouseY + 14;
        if (boxX + boxWidth > getWidth()) {
            boxX = getWidth() - boxWidth - 2;
        }
        if (boxY + boxHeight > getHeight()) {
            boxY = getHeight() - boxHeight - 2;
        }

        g.setColor(TOOLTIP_BG);
        g.fillRect(boxX, boxY, boxWidth, boxHeight);
        g.setColor(TOOLTIP_BORDER);
        g.setStroke(new BasicStroke(2f));
        g.drawRect(boxX, boxY, boxWidth, boxHeight);

        int textX = boxX + padding;
        int textY = boxY + padding + g.getFontMetrics().getAscent();
        for (String line : lines) {
            g.setColor(Color.WHITE);
            g.drawString(line, textX, textY);
            textY += lineHeight;
        }
    }

    private void drawCenteredMessage(Graphics2D g, String message) {
        g.setFont(pixelFont.deriveFont(Font.BOLD, 22f));
        g.setColor(new Color(230, 230, 240));
        int width = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (getWidth() - width) / 2, getHeight() / 2);
    }
}
