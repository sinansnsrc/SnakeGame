import processing.core.PApplet;

public class Main extends PApplet {
    //Fields
    public static final int WINDOWHEIGHT = 800;
    public static final int WINDOWWIDTH = 800;
    public static final int WINDOWBORDER = 50;

    private long updateTime = 0L;
    private boolean gamePaused = false;

    private SnakeGame snakeGame;

    public static void main(String[] args) {
        PApplet.main(new String[] {"Main"});
    }

    //Processing Methods
    public void settings(){
        size(WINDOWHEIGHT,WINDOWWIDTH);
        smooth(8);
    }

    public void setup(){
        colorMode(HSB, 360, 100, 100);
        rectMode(RADIUS);

        surface.setResizable(false);
        surface.setTitle("Snake!");

        createGame();
    }

    public void draw(){
        if(snakeGame.isGameInitialized()) {
            if(!gamePaused && millis() >= updateTime) {
                updateTime = millis() + snakeGame.getDelayTime();

                snakeGame.updateGame();
            }
        }
        snakeGame.drawGame();
    }

    public void keyPressed() {
        //Directional Inputs
        if(key == 'w' || (key == CODED && keyCode == UP)) {
            snakeGame.updateGameInput(new int[] {0,-1});
        }
        else if(key == 'a' || (key == CODED && keyCode == LEFT)) {
            snakeGame.updateGameInput(new int[] {-1,0});
        }
        else if(key == 's' || (key == CODED && keyCode == DOWN)) {
            snakeGame.updateGameInput(new int[] {0,1});
        }
        else if(key == 'd' || (key == CODED && keyCode == RIGHT)) {
            snakeGame.updateGameInput(new int[] {1,0});
        }

        //In-Game Modifier Inputs
        if(key == '=') {
            snakeGame.increaseDelayTime();
        }

        if(key == '-') {
            snakeGame.decreaseDelayTime();
        }

        if(key == 'p') {
            gamePaused = !gamePaused;
        }

        //Termination/Restart Inputs
        if(snakeGame.isGameInitialized() && key == 'r') {
            snakeGame.restartGame();
        }

        if(!snakeGame.isGameInitialized() && key == ENTER) {
            snakeGame.initializeGame();
        }

        if(snakeGame.isGameInitialized() && snakeGame.checkGameOver() && key == ENTER) {
            snakeGame.restartGame();
        }
    }

    //Class Methods
    public void createGame() {
        snakeGame = new SnakeGame(this);
    }
}
