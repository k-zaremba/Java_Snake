import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class Player {

    final static int MAX_POW_TICKS = 30;
    private final String name;
    private int score;
    private int snakeLen;
    private boolean isDead;
    private boolean isPoweredUp;
    private int remainingPowTicks = 0;
    private int curMaxPowTicks = 0;
    private int totalWins = 0;
    private Color snakeColor;
    private Color snakeHeadColor;

    private final int[] xPos = new int[GamePanel.TOTAL_FIELDS];
    private final int[] yPos = new int[GamePanel.TOTAL_FIELDS];

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;

    enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    Player(String name, Color snakeColor, Color snakeHeadColor){
        this.name = name;
        this.snakeColor = snakeColor;
        this.snakeHeadColor = snakeHeadColor;
        this.isDead = false;
        this.isPoweredUp = false;
    }

    public void move(Player[] players) {
        if(remainingPowTicks > 0)
            remainingPowTicks--;
        else
            isPoweredUp = false;
        direction = nextDirection;
        for (int i = snakeLen; i > 0; i--) {
            xPos[i] = xPos[i - 1];
            yPos[i] = yPos[i - 1];
        }

        if (direction == Player.Direction.RIGHT)
            xPos[0] = xPos[0] + GamePanel.SEGMENT_SIZE;
        if (direction == Player.Direction.LEFT)
            xPos[0] = xPos[0] - GamePanel.SEGMENT_SIZE;
        if (direction == Player.Direction.UP)
            yPos[0] = yPos[0] - GamePanel.SEGMENT_SIZE;
        if (direction == Player.Direction.DOWN)
            yPos[0] = yPos[0] + GamePanel.SEGMENT_SIZE;

        checkCollision(players);
    }

    public void checkCollision(Player[] players) {
        checkApple();
        checkStar();
        if(isPoweredUp)
            goThroughWalls();
        else{
            checkWalls();
            checkSelf();
            checkEnemy(players);
        }
    }

    public void checkStar(){
        if (xPos[0] == GamePanel.starX && yPos[0] == GamePanel.starY) {
            GamePanel.isStarEaten = true;
            isPoweredUp = true;

            remainingPowTicks = Math.min(snakeLen * 2, MAX_POW_TICKS);
            curMaxPowTicks = remainingPowTicks;
        }
    }

    public void checkApple(){
        if (xPos[0] == GamePanel.appleX && yPos[0] == GamePanel.appleY) {
            snakeLen++;
            score++;
            GamePanel.isAppleEaten = true;
        }
    }

    public void checkSelf() {
        //checks if head collides with body
        for (int i = snakeLen; i > 0; i--) {
            if ((xPos[0] == xPos[i]) && (yPos[0] == yPos[i])) {
                GamePanel.running = false;
                isDead = true;
                break;
            }
        }
    }

    public void checkWalls(){
        //check if head touches left border
        if (xPos[0] < 0) {
            GamePanel.running = false;
            isDead = true;
        }
        //check if head touches right border
        if (xPos[0] > GamePanel.WINDOW_WIDTH - GamePanel.SEGMENT_SIZE) {
            GamePanel.running = false;
            isDead = true;
        }
        //check if head touches top border
        if (yPos[0] < 0) {
            GamePanel.running = false;
            isDead = true;
        }
        //check if head touches bottom border
        if (yPos[0] > GamePanel.WINDOW_HEIGHT - GamePanel.SEGMENT_SIZE) {
            GamePanel.running = false;
            isDead = true;
        }
    }


    public void goThroughWalls(){
        //check if head touches left border
        if (xPos[0] < 0)
            xPos[0] = GamePanel.WINDOW_WIDTH - GamePanel.SEGMENT_SIZE;
        //check if head touches right border
        if (xPos[0] > GamePanel.WINDOW_WIDTH - GamePanel.SEGMENT_SIZE)
            xPos[0] = 0;
        //check if head touches top border
        if (yPos[0] < 0)
            yPos[0] = GamePanel.WINDOW_HEIGHT - GamePanel.SEGMENT_SIZE;
        //check if head touches bottom border
        if (yPos[0] > GamePanel.WINDOW_HEIGHT - GamePanel.SEGMENT_SIZE)
            yPos[0] = 0;
    }

    void checkEnemy(Player[] players){
        //checks if head collides with body
        for(Player p : players) {
            if(p != this)
                for (int i = p.snakeLen; i >= 0; i--) {
                    if ((xPos[0] == p.xPos[i]) && (yPos[0] == p.yPos[i])) {
                        GamePanel.running = false;
                        isDead = true;
                        break;
                    }
                }
        }
    }

    public Color getSnakeColor() {
        return snakeColor;
    }

    public Color getSnakeHeadColor() {
        return snakeHeadColor;
    }

    public int getCurMaxPowTicks() {
        return curMaxPowTicks;
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getNextDirection() {
        return nextDirection;
    }
    public int getRemainingPowTicks() {
        return remainingPowTicks;
    }

    public int getScore() {
        return score;
    }

    public int getSnakeLen() {
        return snakeLen;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getxPos(int i) {
        return xPos[i];
    }

    public int getyPos(int i) {
        return yPos[i];
    }

    public String getName() {
        return name;
    }

    public boolean isDead(){
        return isDead;
    }

    public boolean isPoweredUp() {
        return isPoweredUp;
    }

    public void setCurMaxPowTicks(int curMaxPowTicks) {
        this.curMaxPowTicks = curMaxPowTicks;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPoweredUp(boolean poweredUp) {
        isPoweredUp = poweredUp;
    }

    public void setRemainingPowTicks(int remainingPowTicks) {
        this.remainingPowTicks = remainingPowTicks;
    }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSnakeColor(Color snakeColor) {
        this.snakeColor = snakeColor;
    }

    public void setSnakeHeadColor(Color snakeHeadColor) {
        this.snakeHeadColor = snakeHeadColor;
    }

    public void setSnakeLen(int snakeLen) {
        this.snakeLen = snakeLen;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setxPos(int i, int val){
        this.xPos[i] = val;
    }

    public void setyPos(int i, int val){
        this.yPos[i] = val;
    }
}
