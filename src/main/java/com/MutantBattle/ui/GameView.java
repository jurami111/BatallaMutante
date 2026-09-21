package com.MutantBattle.ui;

import com.MutantBattle.config.constants;
import com.MutantBattle.control.BattleEngine;
import com.MutantBattle.game.Battlefield;
import com.MutantBattle.game.BattlefieldFactory;
import com.MutantBattle.game.Team;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

// Ventana principal (Vista) del juego. Lanza la batalla en un hilo aparte y refresca
// el lienzo y las estadisticas mediante un Timer de Swing al ritmo configurado.
// Solo consulta el estado del modelo; no implementa logica de juego.
public class GameView extends JFrame {

    private static final Color BAR_BG = new Color(28, 28, 36);
    private static final Color TEXT = new Color(235, 235, 245);
    private static final Color RED_TEAM = new Color(214, 69, 65);
    private static final Color BLUE_TEAM = new Color(72, 118, 214);

    private final BattlefieldPanel panel = new BattlefieldPanel();
    private final SidePanel sidePanel = new SidePanel();
    private final JSpinner teamSizeSpinner = new JSpinner(
            new SpinnerNumberModel(constants.MIN_TEAM_SIZE, constants.MIN_TEAM_SIZE, constants.MAX_TEAM_SIZE, 1));
    private final JButton startButton = new JButton("Start Battle");
    private final JLabel redLabel = new JLabel();
    private final JLabel blueLabel = new JLabel();
    private final JLabel winnerLabel = new JLabel();

    private transient Battlefield battlefield;
    private transient Thread battleThread;
    private Timer refreshTimer;

    public GameView() {
        super("Mutant Battle");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);

        panel.setPreferredSize(new Dimension(constants.BATTLEFIELD_WIDTH, constants.BATTLEFIELD_HEIGHT));
        add(panel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        startButton.addActionListener(e -> startBattle());

        refreshTimer = new Timer(constants.REFRESH_RATE_MILLISECONDS, e -> onTick());
        refreshTimer.start();

        pack();
        setLocationRelativeTo(null);
        updateStats();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bar.setBackground(BAR_BG);

        JLabel sizeLabel = label("Team size:", TEXT);
        teamSizeSpinner.setPreferredSize(new Dimension(60, 26));

        startButton.setFont(retro(Font.BOLD, 13));

        redLabel.setFont(retro(Font.BOLD, 13));
        blueLabel.setFont(retro(Font.BOLD, 13));
        winnerLabel.setFont(retro(Font.BOLD, 15));
        redLabel.setForeground(RED_TEAM);
        blueLabel.setForeground(BLUE_TEAM);
        winnerLabel.setForeground(new Color(240, 210, 60));

        bar.add(sizeLabel);
        bar.add(teamSizeSpinner);
        bar.add(startButton);
        bar.add(Box.createHorizontalStrut(16));
        bar.add(redLabel);
        bar.add(blueLabel);
        bar.add(Box.createHorizontalStrut(16));
        bar.add(winnerLabel);
        return bar;
    }

    private void startBattle() {
        if (battleThread != null && battleThread.isAlive()) {
            return;
        }
        winnerLabel.setText("");
        EventLog.get().clear();
        int teamSize = (Integer) teamSizeSpinner.getValue();
        battlefield = new BattlefieldFactory().crearCampo(teamSize);
        panel.setBattlefield(battlefield);
        sidePanel.setBattlefield(battlefield);

        BattleEngine engine = new BattleEngine(battlefield);
        battleThread = new Thread(engine::runUntilFinished, "BattleEngine");
        battleThread.setDaemon(true);
        battleThread.start();

        startButton.setEnabled(false);
        teamSizeSpinner.setEnabled(false);
    }

    // Se ejecuta en el hilo de la interfaz: redibuja el campo y refresca el marcador.
    private void onTick() {
        if (battlefield != null) {
            panel.repaint();
            sidePanel.refresh();
            updateStats();
        }
        if (battleThread != null && !battleThread.isAlive() && !startButton.isEnabled()) {
            finishBattle();
        }
    }

    private void finishBattle() {
        startButton.setEnabled(true);
        teamSizeSpinner.setEnabled(true);
        startButton.setText("New Battle");
        Team winner = battlefield.getWinner();
        winnerLabel.setText(winner != null ? "WINNER: " + winner.getName() + "!" : "Tie game!");
    }

    private void updateStats() {
        if (battlefield == null) {
            redLabel.setText("Red  -/-");
            blueLabel.setText("Blue  -/-");
            return;
        }
        Team red = battlefield.getTeam1();
        Team blue = battlefield.getTeam2();
        redLabel.setText(String.format("%s  alive:%d dead:%d  score:%d",
                red.getName(), red.getAliveMutantsCount(), red.getDeadMutantsCount(), red.getScore()));
        blueLabel.setText(String.format("%s  alive:%d dead:%d  score:%d",
                blue.getName(), blue.getAliveMutantsCount(), blue.getDeadMutantsCount(), blue.getScore()));
    }

    private JLabel label(String text, Color color) {
        JLabel jLabel = new JLabel(text);
        jLabel.setFont(retro(Font.BOLD, 13));
        jLabel.setForeground(color);
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        return jLabel;
    }

    private Font retro(int style, int size) {
        return new Font("Monospaced", style, size);
    }
}
