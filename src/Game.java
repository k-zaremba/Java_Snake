import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame implements ActionListener {

    ScorePanel scorePanel;
    GamePanel gamePanel;
    Player[] players;
    Timer timer;
    Audio audio;
    boolean isMuted;

    Game(boolean startMuted) {
        this.setTitle("SnakeGame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.isMuted = startMuted;

        Player p1 = new Player("Player1", new Color(21, 96, 55), new Color(28, 198, 100));
        Player p2 = new Player("Player2", new Color(71, 17, 117), new Color(115, 20, 203));
        players = new Player[]{p1, p2};

        timer = new Timer(GamePanel.DELAY, this);
        audio = new Audio("media\\music_8bit_jumpy.wav");
        scorePanel = new ScorePanel(players);
        gamePanel = new GamePanel(players, timer);
        timer.start();

        this.add(scorePanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.SOUTH);
        scorePanel.resetButton.addActionListener(this);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if (!isMuted)
            audio.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

        @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == scorePanel.resetButton) {
            gamePanel.restartGame();
        }

        if (GamePanel.running) {

            for (Player p : players) p.move(players);

            gamePanel.checkStarCondition();
            gamePanel.checkAppleCondition();
            // modifications of point due to players ending the game
            boolean allDead = true;
            for (Player p : players) {
                if (p.isDead()) {
                    int penalty;
                    if(p.getScore() < 10)
                        penalty = p.getScore() + 1;
                    else
                        penalty = p.getScore() / 3;
                    p.setScore(p.getScore() -  penalty);
                } else {
                    allDead = false;
                }
            }

            if (allDead) {
                for (Player p : players) {
                    p.setScore(0);
                }
                System.out.println("BOTH DIED AT THE SAME TIME.");
            }

            scorePanel.scoreLabelP1.setText(players[0].getName() + ": " + players[0].getScore());
            scorePanel.scoreLabelP2.setText(players[1].getName() + ": " + players[1].getScore());

            gamePanel.repaint();
        }

        if (!GamePanel.running) {
            gamePanel.repaint();
        }
    }
}