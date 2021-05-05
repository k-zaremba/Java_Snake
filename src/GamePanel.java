import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel{
    final static int WINDOW_HEIGHT = 500;
    final static int WINDOW_WIDTH = 500;
    final static int DELAY = 140;

    final static int SEGMENT_SIZE = 25;
    final static int TOTAL_SEGMENTS = (WINDOW_HEIGHT / SEGMENT_SIZE);
    final static int TOTAL_FIELDS = TOTAL_SEGMENTS * TOTAL_SEGMENTS;
    final static int OUTLINE_THICKNESS = SEGMENT_SIZE / 6;
    final static int OUT_OF_MAP = -5000;
    static Boolean running = false;
    static Boolean inLobby = false;
    final int STARTING_SNAKE_LEN = 2;

    static boolean isAppleEaten = false;
    static int appleX;
    static int appleY;
    static boolean isStarEaten = true;


    static final int STAR_TTL = 20;
    int starTTL = STAR_TTL;
    static int starX;
    static int starY;
    Player[] players;
    Timer timer;

    Image appleImage = new ImageIcon("media\\pepe.png").getImage();
    Image starImage = new ImageIcon("media\\redstar.png").getImage();

    GamePanel(Player[] players, Timer timer) {
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(new Color(20,20,20));
        this.setFocusable(true);
        this.setOpaque(true);
        this.addKeyListener(new ControlKeyAdapter());
        this.players = players;
        this.timer = timer;
        this.init();
    }

    public void init(){

        for(int i = 0; i < players.length; i++) {
            if(i == 0)
                initPlayer(players[0], SEGMENT_SIZE);
            else
                initPlayer(players[i], WINDOW_HEIGHT / i - (i * SEGMENT_SIZE + SEGMENT_SIZE));

        }
        running = false;
        inLobby = true;
        isStarEaten = true;
        genApple();
    }

    public void initPlayer(Player player, int yPosStarting) {
        for(int i = 0; i < TOTAL_FIELDS; i++){
            player.setxPos(i,0);
            player.setyPos(i,0);
        }
        player.setSnakeLen(STARTING_SNAKE_LEN);
        player.setScore(0);
        player.setDirection(Player.Direction.RIGHT);
        player.setNextDirection(Player.Direction.RIGHT);
        player.setDead(false);
        player.setPoweredUp(false);
        for (int i = 0; i < player.getSnakeLen(); i++) {
            player.setxPos(i,(player.getSnakeLen() - i - 1) * SEGMENT_SIZE);
            player.setyPos(i, yPosStarting);
        }
    }

    @Override
    public void paint(Graphics graphics) {

        if (running) {
            super.paint(graphics);

            graphics.drawImage(appleImage, appleX + OUTLINE_THICKNESS, appleY + OUTLINE_THICKNESS,
                    SEGMENT_SIZE - 2*OUTLINE_THICKNESS, SEGMENT_SIZE - 2*OUTLINE_THICKNESS, null);

            if(!isStarEaten)
                graphics.drawImage(starImage, starX + OUTLINE_THICKNESS, starY + OUTLINE_THICKNESS,
                    SEGMENT_SIZE - 2*OUTLINE_THICKNESS, SEGMENT_SIZE - 2*OUTLINE_THICKNESS, null);
            for(Player player : players) {
                for (int i = 0; i < player.getSnakeLen(); i++) {
                    if (i == 0)
                        graphics.setColor(player.getSnakeHeadColor());
                    else {
                        if(player.isPoweredUp() && i <= ((double)player.getRemainingPowTicks() / player.getCurMaxPowTicks()) * player.getSnakeLen()) {
                            Random random = new Random();
                            int[] rgb = new int[3];
                            rgb[0] = random.nextInt(100) + 156; // red
                            rgb[1] = random.nextInt(100) + 156; // green
                            rgb[2] = random.nextInt(100) + 156; // blue

                            rgb[random.nextInt(3)] = random.nextInt(150);
                            graphics.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        }
                        else
                            graphics.setColor(player.getSnakeColor());
                    }

                    graphics.fillRect(player.getxPos(i), player.getyPos(i), SEGMENT_SIZE, SEGMENT_SIZE);

                    // SNAKE SEGMENT OUTLINE
                    graphics.setColor(ColorSheet.gray);
                    graphics.fillRect(player.getxPos(i),player.getyPos(i), SEGMENT_SIZE, OUTLINE_THICKNESS);
                    graphics.fillRect(player.getxPos(i),player.getyPos(i), OUTLINE_THICKNESS, SEGMENT_SIZE);
                    graphics.fillRect(player.getxPos(i),player.getyPos(i) + SEGMENT_SIZE - OUTLINE_THICKNESS, SEGMENT_SIZE, OUTLINE_THICKNESS);
                    graphics.fillRect(player.getxPos(i) + SEGMENT_SIZE - OUTLINE_THICKNESS, player.getyPos(i), OUTLINE_THICKNESS, SEGMENT_SIZE);
                }
            }
        }else if(inLobby) {
            displayStarter(graphics);
        }
        else{
            displayGameOver(graphics);
        }
    }

    public void displayStarter(Graphics graphics){
        super.paint(graphics);
        String startingMessage = "PRESS SPACE TO START";
        graphics.setFont(new Font("Sans Serif", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.setColor(ColorSheet.berry);
        final int PADDING = 5;
        graphics.fillRoundRect((WINDOW_WIDTH - metrics.stringWidth(startingMessage)) / 2 - PADDING, WINDOW_HEIGHT/4, metrics.stringWidth(startingMessage) + 2 * PADDING, WINDOW_HEIGHT * 3 / 16, 40, 40);
        graphics.setColor(ColorSheet.orange);
        graphics.drawRoundRect((WINDOW_WIDTH - metrics.stringWidth(startingMessage)) / 2 - PADDING, WINDOW_HEIGHT/4, metrics.stringWidth(startingMessage) + 2 * PADDING, WINDOW_HEIGHT * 3 / 16, 40, 40);
        graphics.drawString(startingMessage, (WINDOW_WIDTH - metrics.stringWidth(startingMessage)) / 2, WINDOW_HEIGHT * 3 / 8);
    }

    public void displayGameOver(Graphics graphics){
        super.paint(graphics);
        String gameOverMessage = "";
        boolean isNoWinner = true;

        for(Player p : players){
            for(Player o : players){
                if(p != o){
                    if(p.getScore() > o.getScore()){
                        gameOverMessage = p.getName()+ " WINS!";
                        p.setTotalWins(p.getTotalWins()+1);
                        isNoWinner = false;
                    }
                }
            }
        }
        if(isNoWinner)
            gameOverMessage = "DRAW!";

        System.out.println("TOTAL WINS:");
        for(Player p : players)
            System.out.println(p.getName() + ": " + p.getTotalWins());

        graphics.setFont(new Font("Sans Serif", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(graphics.getFont());

        graphics.setColor(ColorSheet.berry);

        final int PADDING = 5;
        graphics.fillRoundRect((WINDOW_WIDTH - metrics.stringWidth(gameOverMessage)) / 2 - PADDING, WINDOW_HEIGHT/4, metrics.stringWidth(gameOverMessage) + 2 * PADDING, WINDOW_HEIGHT * 3 / 16, 40, 40);
        graphics.setColor(ColorSheet.orange);
        graphics.drawRoundRect((WINDOW_WIDTH - metrics.stringWidth(gameOverMessage)) / 2 - PADDING, WINDOW_HEIGHT/4, metrics.stringWidth(gameOverMessage) + 2 * PADDING, WINDOW_HEIGHT * 3 / 16, 40, 40);
        graphics.drawString(gameOverMessage, (WINDOW_WIDTH - metrics.stringWidth(gameOverMessage)) / 2, WINDOW_HEIGHT * 3 / 8);
        timer.stop();
    }

    public void checkAppleCondition(){
        if(isAppleEaten)
            genApple();
    }

    public void genApple(){
        Random random = new Random();
        boolean gen = false;
        while(!gen) {
            appleX = random.nextInt(WINDOW_WIDTH / SEGMENT_SIZE) * SEGMENT_SIZE;
            appleY = random.nextInt(WINDOW_HEIGHT / SEGMENT_SIZE) * SEGMENT_SIZE;
            gen = true;

            for(Player player : players) {
                for (int i = 0; i < player.getSnakeLen(); i++)
                    if (appleX == player.getxPos(i) && appleY == player.getyPos(i)) {
                        gen = false;
                        break;
                    }
            }
        }
        isAppleEaten = false;
    }

    public void checkStarCondition(){

        if(isStarEaten){
            Random random = new Random();
            int genNum = random.nextInt(1000);
            if(genNum < 10) {
                starTTL = STAR_TTL;
                genStar();
            }
        }else{
            starTTL--;
            if(starTTL < 0){
                isStarEaten = true;
                starX = OUT_OF_MAP;
                starY = OUT_OF_MAP;
            }
        }
    }

    public void genStar(){
        Random random = new Random();
        boolean gen = false;
        while(!gen) {
            starX = random.nextInt(WINDOW_WIDTH / SEGMENT_SIZE) * SEGMENT_SIZE;
            starY = random.nextInt(WINDOW_HEIGHT / SEGMENT_SIZE) * SEGMENT_SIZE;
            gen = true;

            for(Player player : players) {
                for (int i = 0; i < player.getSnakeLen(); i++)
                    if (starX == player.getxPos(i) && starY == player.getyPos(i)) {
                        gen = false;
                        break;
                    }
            }
        }
        isStarEaten = false;
    }

    public void restartGame(){
        setInLobby(true);
        setRunning(false);
        init();
        timer.restart();
    }

    public static void setRunning(Boolean running) {
        GamePanel.running = running;
    }

    public static void setInLobby(Boolean inLobby) {
        GamePanel.inLobby = inLobby;
    }

    public class ControlKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if(players[0].getDirection() != Player.Direction.RIGHT) {
                         players[0].setNextDirection(Player.Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_D:
                    if(players[0].getDirection() != Player.Direction.LEFT) {
                        players[0].setNextDirection(Player.Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_W:
                    if(players[0].getDirection() != Player.Direction.DOWN) {
                        players[0].setNextDirection(Player.Direction.UP);
                    }
                    break;
                case KeyEvent.VK_S:
                    if(players[0].getDirection() != Player.Direction.UP) {
                        players[0].setNextDirection(Player.Direction.DOWN);
                    }
                    break;
                case KeyEvent.VK_M: // left
                    if(players[1].getDirection() != Player.Direction.RIGHT) {
                        players[1].setNextDirection(Player.Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_O: // right
                    if(players[1].getDirection() != Player.Direction.LEFT) {
                        players[1].setNextDirection(Player.Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_I: // up
                    if(players[1].getDirection() != Player.Direction.DOWN) {
                        players[1].setNextDirection(Player.Direction.UP);
                    }
                    break;
                case KeyEvent.VK_K: // down
                    if(players[1].getDirection() != Player.Direction.UP) {
                        players[1].setNextDirection(Player.Direction.DOWN);
                    }
                    break;
                case (KeyEvent.VK_U):
                case (KeyEvent.VK_R):
                    restartGame();
                    break;
                case KeyEvent.VK_0:
                    if(timer.isRunning())
                        timer.stop();
                    else
                        timer.start();
                    break;
                case KeyEvent.VK_SPACE:
                    GamePanel.setRunning(true);
                    GamePanel.setInLobby(false);
            }
        }
    }

}
