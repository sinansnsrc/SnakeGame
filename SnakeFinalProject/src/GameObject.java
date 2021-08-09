import processing.core.PApplet;

public abstract class GameObject {
    //Fields
    private int xPosition, yPosition;
    private int[] color;
    private int mapSize;
    private boolean disabled;
    public PApplet sketch;

    //Constructor
    public GameObject(int[] arrayPosition, int[] color, PApplet sketch, int mapSize) {
        setArrayPosition(arrayPosition);
        this.color = color;
        this.sketch = sketch;
        this.mapSize = mapSize;
    }

    //Getter and Setter Methods
    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int x) {
        this.xPosition = x;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int y) {
        this.yPosition = y;
    }

    public void setArrayPosition(int[] arrayPosition) {
        setXPosition(arrayPosition[0]);
        setYPosition(arrayPosition[1]);
    }

    public int[] getArrayPosition() {
        return new int[] { getXPosition(), getYPosition() };
    }

    public float getScale() {
        return SnakeGame.worldToScreenSize(1f, mapSize);
    }

    public int[] getColor() {
        return color;
    }

    public int getMapSize() {
        return mapSize;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    //Abstract Methods
    public abstract void drawGameObject();
}
