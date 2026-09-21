package com.MutantBattle;

//Ui Enhancement
import com.MutantBattle.ui.GameView;
import javax.swing.SwingUtilities;
//Ui Enhancement

public class Main {

    public static void main(String[] args) {
        //Ui Enhancement
        // La interfaz grafica (JFrame) reemplaza el bucle de consola: pide el tamano
        // de equipo desde la ventana y dibuja la batalla en tiempo real estilo pixel.
        SwingUtilities.invokeLater(() -> {
            GameView window = new GameView();
            window.setVisible(true);
        });
        //Ui Enhancement
    }
}