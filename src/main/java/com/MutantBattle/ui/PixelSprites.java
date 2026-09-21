package com.MutantBattle.ui;

import com.MutantBattle.model.BaseMutant;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

// Almacen de sprites estilo pixel-art (8x8) para cada tipo de mutante.
// Cada sprite es un arreglo de 8 cadenas de 8 caracteres, donde cada caracter es un color logico:
//   '.' transparente   'X' contorno oscuro   'B' cuerpo (color del equipo)
//   'S' sombra          'E' detalle claro     'A' acento (elemento/arma del tipo)
public final class PixelSprites {

    public static final int GRID = 8;

    private static final Color OUTLINE = new Color(24, 22, 30);
    private static final Color EYE = new Color(245, 245, 245);

    private static final Map<String, String[]> SPRITES = new HashMap<>();
    private static final Map<String, Color> ACCENTS = new HashMap<>();

    static {
        SPRITES.put("Archer", new String[] {
                "..XXX...",
                ".XBBBX..",
                ".XBEBX..",
                "XAABBBX.",
                ".XBBBXA.",
                ".XBBBXA.",
                ".XB.BX..",
                "..X.X..."
        });
        ACCENTS.put("Archer", new Color(150, 100, 40));

        SPRITES.put("Dragon", new String[] {
                ".X....X.",
                ".XX..XX.",
                "..XBBX..",
                ".XBBBBX.",
                "XBBEBBBX",
                ".XBBBBX.",
                "..XAAX..",
                "...XX..."
        });
        ACCENTS.put("Dragon", new Color(255, 140, 0));

        SPRITES.put("Heterodactyl", new String[] {
                "A......A",
                "AX....XA",
                ".AXBBXA.",
                "..XBBX..",
                "..XBEX..",
                "..XBBX..",
                "...XX...",
                "..A..A.."
        });
        ACCENTS.put("Heterodactyl", new Color(120, 220, 220));

        SPRITES.put("Monster", new String[] {
                "........",
                ".XXXXX..",
                "XBBBBBX.",
                "XBEBEBX.",
                "XBBBBBX.",
                "XEAEAEX.",
                ".XXXXX..",
                "..X.X..."
        });
        ACCENTS.put("Monster", new Color(120, 200, 80));

        SPRITES.put("SuperHuman", new String[] {
                "..XXX...",
                ".XBBBX..",
                ".XBEBX..",
                "AABBBAA.",
                ".XBBBXA.",
                ".AXBXA..",
                ".XB.BX..",
                "..X.X..."
        });
        ACCENTS.put("SuperHuman", new Color(240, 210, 60));
    }

    private PixelSprites() {
    }

    private static final String[] FALLBACK = {
            "........",
            "..XXXX..",
            ".XBBBBX.",
            ".XBEBEX.",
            ".XBBBBX.",
            ".XBBBBX.",
            "..XXXX..",
            "........"
    };

    public static String[] spriteFor(BaseMutant mutant) {
        return SPRITES.getOrDefault(mutant.getClass().getSimpleName(), FALLBACK);
    }

    public static Color accentFor(BaseMutant mutant) {
        return ACCENTS.getOrDefault(mutant.getClass().getSimpleName(), Color.LIGHT_GRAY);
    }

    // Dibuja el sprite escalado por 'pixel' con la esquina superior izquierda en (x, y).
    public static void draw(Graphics2D g, String[] sprite, int x, int y, int pixel, Color body, Color accent) {
        Color shade = body.darker();
        for (int row = 0; row < sprite.length; row++) {
            String line = sprite[row];
            for (int col = 0; col < line.length(); col++) {
                Color color = colorFor(line.charAt(col), body, shade, accent);
                if (color == null) {
                    continue;
                }
                g.setColor(color);
                g.fillRect(x + col * pixel, y + row * pixel, pixel, pixel);
            }
        }
    }

    private static Color colorFor(char c, Color body, Color shade, Color accent) {
        switch (c) {
            case 'X':
                return OUTLINE;
            case 'B':
                return body;
            case 'S':
                return shade;
            case 'E':
                return EYE;
            case 'A':
                return accent;
            default:
                return null;
        }
    }
}
