import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ScorePanel extends JPanel {

    static final int WINDOW_WIDTH = GamePanel.WINDOW_WIDTH;
    static final int WINDOW_HEIGHT = 40;
    JLabel scoreLabelP1;
    JLabel scoreLabelP2;
    ResetButton resetButton;
    final static String FONT_FAMILY  = "Sans Serif";

    ScorePanel(Player[] players){
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout(50, 0));

        this.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200,0,100), 4, true), new EmptyBorder(0,30,0,30)));
        this.setFocusable(false);
        this.setOpaque(true);

        scoreLabelP1 = new JLabel();
        scoreLabelP1.setText(players[0].getScore() +": "+ 0);
        scoreLabelP1.setForeground(Color.WHITE);
        scoreLabelP1.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));

        scoreLabelP2 = new JLabel();
        scoreLabelP2.setText(players[1].getScore() + ": " + 1);
        scoreLabelP2.setForeground(Color.WHITE);
        scoreLabelP2.setFont(new Font(FONT_FAMILY, Font.BOLD, 20));

        resetButton = new ResetButton();
        this.add(scoreLabelP1, BorderLayout.WEST);
        this.add(resetButton, BorderLayout.CENTER);
        this.add(scoreLabelP2, BorderLayout.EAST);
    }

}
