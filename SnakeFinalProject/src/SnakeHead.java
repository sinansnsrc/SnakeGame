import processing.core.PApplet;
import java.util.ArrayList;

public class SnakeHead extends GameObject {
    //Fields
    public int[] currentHeading;
    public int[] potentialHeading;

    private int amountToGrow;

    private ArrayList<int[]> positions;
    private ArrayList<SnakeBody> snakeBodies;

    private boolean colorIncreasing;
    private int colorModifier;
    private boolean screenWrapping;

    //Constructor
    public SnakeHead(int[] arrayPosition, int[] color, PApplet sketch, int initialSize, int mapSize, boolean wrapScreen) {
        super(arrayPosition, color, sketch, mapSize);

        screenWrapping = wrapScreen;

        colorIncreasing = true;
        colorModifier = 0;

        currentHeading = new int[] {1,0};
        potentialHeading = new int[] {1,0};

        positions = new ArrayList<>();
        snakeBodies = new ArrayList<>();
        for(int i = 0; i < initialSize; i++) {
            addSnakeBody();
        }
    }
    
    //Implemented Abstract Method
    @Override
    public void drawGameObject() {
        for(int i = snakeBodies.size() - 1; i >= 0; i--) {
            snakeBodies.get(i).drawGameObject();
        }

        sketch.fill(getColor()[0],getColor()[1],getColor()[2]);
        float[] screenPosition = SnakeGame.worldToScreenPosition(new int[] {getXPosition(), getYPosition()}, getMapSize());
        sketch.rect(screenPosition[0], screenPosition[1], getScale(), getScale(),12);

        if(currentHeading[1] == 1) { //UP
            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] + (getScale() / 2), screenPosition[1] + (getScale() / 4), getScale() / 3, getScale() / 3, 8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] + (getScale() / 2), screenPosition[1] + (getScale() / 4), getScale() / 6, getScale() / 6, 16);

            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] - (getScale() / 2), screenPosition[1] + (getScale() / 4), getScale() / 3, getScale() / 3, 8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] - (getScale() / 2), screenPosition[1] + (getScale() / 4), getScale() / 6, getScale() / 6, 16);
        }
        else if(currentHeading[0] == 1) { //RIGHT
            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] + (getScale() / 4), screenPosition[1] + (getScale() / 2), getScale() / 3, getScale()/ 3, 8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] + (getScale() / 4), screenPosition[1] + (getScale() / 2), getScale() / 6, getScale() / 6, 16);

            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] + (getScale() / 4), screenPosition[1] - (getScale() / 2), getScale() / 3, getScale() / 3, 8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] + (getScale() / 4), screenPosition[1] - (getScale() / 2), getScale() / 6, getScale() / 6, 16);
        }
        else if(currentHeading[1] == -1) { //DOWN
            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] + (getScale() / 2), screenPosition[1] - (getScale() / 4), getScale() / 3, getScale()/ 3,8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] + (getScale() / 2), screenPosition[1] - (getScale() / 4),getScale() / 6, getScale() / 6, 16);

            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] - (getScale() / 2), screenPosition[1] - (getScale() / 4), getScale() / 3, getScale() / 3,8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] - (getScale() / 2), screenPosition[1] - (getScale() / 4), getScale() / 6, getScale() / 6, 16);
        }
        else if(currentHeading[0] == -1) { //LEFT
            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] - (getScale() / 4), screenPosition[1] + (getScale() / 2), getScale()/ 3, getScale()/ 3,8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] - (getScale() / 4), screenPosition[1] + (getScale() / 2), getScale()/ 6, getScale() / 6, 16);

            sketch.fill(0,0,100);
            sketch.rect(screenPosition[0] - (getScale() / 4), screenPosition[1] - (getScale() / 2), getScale() / 3, getScale() / 3,8);
            sketch.fill(0,0,0);
            sketch.rect(screenPosition[0] - (getScale() / 4), screenPosition[1] - (getScale() / 2), getScale() / 6, getScale() / 6,16);
        }
    }

    //Behaviour Methods
    public void updateHeading(int[] headingDirection) {
        if(headingDirection[1] == 1 && currentHeading[1] != -1) { //UP
            potentialHeading = headingDirection;
        }
        else if(headingDirection[0] == 1 && currentHeading[0] != -1) { //RIGHT
            potentialHeading = headingDirection;
        }
        else if(headingDirection[1] == -1 && currentHeading[1] != 1) { //DOWN
            potentialHeading = headingDirection;
        }
        else if(headingDirection[0] == -1 && currentHeading[0] != 1) { //LEFT
            potentialHeading = headingDirection;
        }
    }

    public void updatePosition() {
        currentHeading = potentialHeading;
        setXPosition(getXPosition() + currentHeading[0]);
        setYPosition(getYPosition() + currentHeading[1]);

        if(screenWrapping && getXPosition() >= getMapSize()) {
            setXPosition(0);
        }
        else if(screenWrapping && getXPosition() < 0) {
            setXPosition(getMapSize() - 1);
        }
        if(screenWrapping && getYPosition() >= getMapSize()) {
            setYPosition(0);
        }
        else if(screenWrapping && getYPosition() < 0) {
            setYPosition(getMapSize() - 1);
        }

        positions.add(0,getArrayPosition());

        if(amountToGrow > 0) {
            amountToGrow--;
            addSnakeBody();
        }

        for(int i = 0; i < snakeBodies.size(); i++) {
            snakeBodies.get(i).setArrayPosition(positions.get(i+1));
        }
    }

    public void setAmountToGrow(int amount) {
        amountToGrow += amount;
    }

    private void addSnakeBody() {
        if(!isDisabled()) {
            if(positions.size() >= 1) {
                positions.add(snakeBodies.get(snakeBodies.size() - 1).getArrayPosition());
            }
            else {
                positions.add(getArrayPosition());
            }

            if(getColor()[0] + colorModifier < 280 && colorIncreasing) {
                colorModifier += 2;
            }
            else if(getColor()[0] + colorModifier > 200 && !colorIncreasing) {
                colorModifier -= 2;
            }
            else {
                colorIncreasing = !colorIncreasing;
            }

            snakeBodies.add(new SnakeBody(positions.get(positions.size() - 1), new int[] {getColor()[0] + colorModifier, getColor()[1], getColor()[2]}, sketch, getMapSize(), 1.0f));


            float tailScaleSize = 0.3f / snakeBodies.size();
            for(int i = 0; i < snakeBodies.size(); i++) {
                snakeBodies.get(i).updateTailSizeModifier(1.0f - (tailScaleSize * i));
            }
        }
    }

    public void removeSnakeBody() {
        if(snakeBodies.size() > 1) { snakeBodies.remove(snakeBodies.size() - 1); }
    }

    //Getter Method
    public int getSize() {
            return 1 + snakeBodies.size();
    }

    //Position/Behaviour Verification Methods
    public boolean isOutOfBounds() {
        return (getXPosition() < 0 || getXPosition() >= getMapSize()) || (getYPosition() < 0 || getYPosition() >= getMapSize());
    }
    
    public boolean isWithinBody(int[] arrayPosition) {
        for(int i = 0; i < snakeBodies.size(); i++) {
            if(arrayPosition[0] == snakeBodies.get(i).getXPosition() && arrayPosition[1] == snakeBodies.get(i).getYPosition()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWithinHead(int[] arrayPosition) {
        if(arrayPosition[0] == getXPosition() && arrayPosition[1] == getYPosition()) {
            return true;
        }
        return false;
    }
}
