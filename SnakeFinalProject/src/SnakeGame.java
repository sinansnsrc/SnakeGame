import processing.core.PApplet;
import java.util.ArrayList;

public class SnakeGame {
    //Fields
    private PApplet sketch;

    private static int highScore;

    private int mapSize, initialSize, numberOfFruit, growthPerFruit;
    private double gameRefreshSpeed;
    private boolean wallCollisions, bodyCollisions;

    private SnakeHead snakeHead;
    private ArrayList<SnakeFruit>  snakeFruits;

    private boolean gameInitialized;
    private int gameInitializeStep;
    private boolean gameOver;

    //Constructor
    public SnakeGame(PApplet sketch) {
        //Instantiation Values For Variables:
        this.sketch = sketch;
        gameInitialized = false;
        gameInitializeStep = 1;
        gameOver = false;

        //Default Values For Game Variables:
        mapSize = 10;
        initialSize = 3;
        numberOfFruit = 1;
        growthPerFruit = 1;
        gameRefreshSpeed = 0.5;
        wallCollisions = true;
        bodyCollisions = true;
    }

    //Behaviour Methods
    public void initializeGame() {
        createGame();
    }

    private void createGame() {
        gameInitialized = true;
        snakeHead = new SnakeHead(new int[] {mapSize / 2 - (mapSize / 4), mapSize / 2}, new int[] {226,69,98}, sketch, initialSize, mapSize, !wallCollisions);
        snakeFruits = new ArrayList<>();
    }

    public void drawGame() {
        //Draws background and borders
        sketch.background(102);
        sketch.background(95, 62, 46);

        sketch.fill(145,83,14);

        sketch.noStroke();

        sketch.rect(Main.WINDOWWIDTH / 2,Main.WINDOWBORDER / 2, Main.WINDOWWIDTH / 2, Main.WINDOWBORDER / 2);
        sketch.rect(Main.WINDOWBORDER / 2,Main.WINDOWHEIGHT / 2, Main.WINDOWBORDER / 2, Main.WINDOWHEIGHT / 2);
        sketch.rect(Main.WINDOWWIDTH - (Main.WINDOWBORDER / 2),Main.WINDOWHEIGHT / 2, Main.WINDOWBORDER / 2, Main.WINDOWHEIGHT / 2);
        sketch.rect(Main.WINDOWWIDTH / 2,Main.WINDOWHEIGHT - (Main.WINDOWBORDER / 2), Main.WINDOWWIDTH / 2, Main.WINDOWBORDER / 2);

        if (!gameInitialized){
            sketch.textAlign(sketch.CENTER, sketch.CENTER);
            sketch.fill(0,0,100);
            sketch.textSize(72);

            sketch.text("Snake!", 400,100, 350, 150);

            sketch.textSize(36);
            sketch.text("By: Sinan Sensurucu", 400,200, 350, 50);

            sketch.fill(120,58,76);
            sketch.rect(400, 225 + gameInitializeStep * 50 + 5f, 300, 25, 16);

            sketch.fill(0,0,100);
            sketch.text("Map Size: " + mapSize, 400, 275, 350, 50);
            sketch.text("Initial Snake Size: " + initialSize, 400, 325, 350, 50);
            sketch.text("Number Of Fruit: " + numberOfFruit, 400, 375, 350, 50);
            sketch.text("Growth Per Fruit: " + growthPerFruit, 400, 425, 350, 50);
            sketch.text("Refresh Speed (seconds): " + gameRefreshSpeed, 400, 475, 350, 50);
            String collisionDisplayString = "";
            if(wallCollisions) {
                collisionDisplayString = "Yes";
            }
            else {
                collisionDisplayString = "No";
            }
            sketch.text("Wall Collisions?: " + collisionDisplayString, 400, 525, 350, 50);
            if(bodyCollisions) {
                collisionDisplayString = "Yes";
            }
            else {
                collisionDisplayString = "No";
            }
            sketch.text("Body Collisions?: " + collisionDisplayString, 400, 575, 350, 50);
            sketch.textSize(40);
            sketch.text("Press enter to start the game...", 400, 650, 350, 50);
            sketch.textSize(24);
            sketch.text("(Press r to restart during the game)", 400, 700, 350, 50);
        }

        else if(gameInitialized && !gameOver) {
            //Initialized Game Text
            sketch.textAlign(sketch.CENTER, sketch.CENTER);
            sketch.fill(0,0,100);
            sketch.textSize(36);
            sketch.text("Score: " + snakeHead.getSize(), Main.WINDOWWIDTH / 2,Main.WINDOWBORDER / 2 - 5f, Main.WINDOWWIDTH / 2, Main.WINDOWBORDER / 2);

            //Draws tiles
            for(int row = 0; row < mapSize; row++) {
                for(int col = 0; col < mapSize; col++) {
                    float[] screenPosition = worldToScreenPosition(new int[] {row, col}, mapSize);
                    float worldSize = worldToScreenSize(1f, mapSize);
                    if((col + row) % 2 == 0) {
                        sketch.fill(120,58,76);
                    }
                    else {
                        sketch.fill(95, 62, 46);

                    }
                    sketch.rect(screenPosition[0], screenPosition[1],(float) worldSize,(float) worldSize);
                }
            }

            //Draws GameObjects
            snakeHead.drawGameObject();

            for(int i = 0; i < snakeFruits.size(); i++) {
                snakeFruits.get(i).drawGameObject();
            }
        }
        else if(gameInitialized && gameOver) {
            snakeHead.setDisabled(true);

            sketch.textAlign(sketch.CENTER, sketch.CENTER);
            sketch.fill(0,0,100);
            sketch.textSize(72);

            sketch.text("Snake!", 400,100, 350, 150);

            sketch.textSize(36);
            sketch.text("By: Sinan Sensurucu", 400,200, 350, 50);

            sketch.textSize(54);
            sketch.text("Score: "  + snakeHead.getSize(), 400, 325, 350, 50);

            sketch.textSize(54);
            sketch.text("High Score: "  + highScore, 400, 475, 350, 50);

            sketch.fill(120,58,76);
            sketch.rect(400, 655, 300, 25, 16);

            sketch.fill(0,0,100);
            sketch.textSize(36);
            sketch.text("Press enter or r to restart...", 400, 650, 700, 50);
        }
    }

    public void restartGame() {
        gameInitialized = false;
        gameInitializeStep = 1;
        gameOver = false;
    }

    public void updateGame() {
        snakeHead.updatePosition();

        for(int i = 0; i < snakeFruits.size(); i++) {
            if(snakeHead.isWithinHead(snakeFruits.get(i).getArrayPosition())) {
                snakeHead.setAmountToGrow(growthPerFruit);
                snakeFruits.remove(i);
                i--;
            }
        }

        while(snakeFruits.size() < numberOfFruit) {
            int[] arrayPosition = new int[] { (int) (Math.random() * mapSize), (int) (Math.random() * mapSize) };
            while(snakeHead.isWithinBody(arrayPosition) || snakeHead.isWithinHead(arrayPosition)) {
                arrayPosition = new int[] { (int) (Math.random() * mapSize), (int) (Math.random() * mapSize) };
            }
            snakeFruits.add(new SnakeFruit(arrayPosition, new int[] {360, 97, 85}, sketch, mapSize));
        }

        if(snakeHead.getSize() == (mapSize * mapSize) - 1) { //max size check
            gameOver = true;
        }
        if(wallCollisions && snakeHead.isOutOfBounds()) { //max size check
            gameOver = true;
        }
        if(bodyCollisions && snakeHead.isWithinBody(snakeHead.getArrayPosition())) {
            gameOver = true;
        }

        if(snakeHead.getSize() > highScore) {
            highScore = snakeHead.getSize();
        }
    }
    
    //Input Handling Method
    public void updateGameInput(int[] direction) {
        if(gameInitialized) {
            snakeHead.updateHeading(direction);
        }
        else {
            if(direction[1] == 1 && gameInitializeStep < 7) { //UP
                gameInitializeStep++;
            }
            else if(direction[0] == 1) { //RIGHT
                switch(gameInitializeStep) {
                    case 1:
                        if(mapSize < 75) {
                            mapSize++;
                        }
                        break;
                    case 2:
                        if(initialSize < 10) {
                            initialSize++;
                        }
                        break;
                    case 3:
                        if(numberOfFruit < 20) {
                            numberOfFruit++;
                        }
                        break;
                    case 4:
                        if(growthPerFruit < 20) {
                            growthPerFruit++;
                        }
                        break;
                    case 5:
                        if(gameRefreshSpeed < 5.0) {
                            gameRefreshSpeed += 0.0625;
                        }
                        break;
                    case 6:
                        wallCollisions = !wallCollisions;
                        break;
                    case 7:
                        bodyCollisions = !bodyCollisions;
                        break;
                }
            }
            else if(direction[1] == -1 && gameInitializeStep > 1) { //DOWN
                gameInitializeStep--;
            }
            else if(direction[0] == -1) { //LEFT
                switch(gameInitializeStep) {
                    case 1:
                        if(mapSize > 3) {
                            mapSize--;
                        }
                        break;
                    case 2:
                        if(initialSize > 1) {
                            initialSize--;
                        }
                        break;
                    case 3:
                        if(numberOfFruit > 1) {
                            numberOfFruit--;
                        }
                        break;
                    case 4:
                        if(growthPerFruit > 1) {
                            growthPerFruit--;
                        }
                        break;
                    case 5:
                        if(gameRefreshSpeed > 0.0625) {
                            gameRefreshSpeed -= 0.0625;
                        }
                        break;
                    case 6:
                        wallCollisions = !wallCollisions;
                        break;
                    case 7:
                        bodyCollisions = !bodyCollisions;
                        break;
                }
            }
        }
    }

    //Information Methods
    public boolean isGameInitialized() {
        return gameInitialized;
    }
    
    public long getDelayTime() { return (long) (gameRefreshSpeed * 1000); }

    public boolean checkGameOver() {
        return gameOver;
    }

    //In-Game Modifier Methods
    public void increaseDelayTime() {
        gameRefreshSpeed += 0.125;
    }

    public void decreaseDelayTime() {
        gameRefreshSpeed -= 0.125;
    }

    public void addSnakeBody() {
        snakeHead.setAmountToGrow(growthPerFruit);
    }

    public void removeSnakeBody() {
        snakeHead.removeSnakeBody();
    }

    //Helper Methods
    public static float[] worldToScreenPosition(int[] mapPosition, int mapSize) {
        return new float[] {
                ((((Main.WINDOWWIDTH - (float) (Main.WINDOWBORDER * 2)) / (float) mapSize)) * (float) mapPosition[0]) + Main.WINDOWBORDER + ((((Main.WINDOWWIDTH - (float) (Main.WINDOWBORDER * 2)) / (float) mapSize)) / 2.0f),
                ((((Main.WINDOWHEIGHT - (float) (Main.WINDOWBORDER * 2)) / (float) mapSize)) * (float) mapPosition[1]) + Main.WINDOWBORDER + ((((Main.WINDOWHEIGHT - (float) (Main.WINDOWBORDER * 2)) / (float) mapSize)) / 2.0f)
        };
    }

    public static float worldToScreenSize(float scale, int mapSize) {
        return (((Main.WINDOWWIDTH - (Main.WINDOWBORDER * 2)) / (float) mapSize) / (float) 2) * scale;
    }
}
