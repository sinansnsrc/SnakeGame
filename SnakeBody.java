import processing.core.PApplet;

public class SnakeBody extends GameObject {
    //Fields
    private float tailSizeModifier;
    
    //Constructor
    public SnakeBody(int[] arrayPosition, int[] color, PApplet sketch, int mapSize, float tailSizeModifier) {
        super(arrayPosition, color, sketch, mapSize);
        this.tailSizeModifier = tailSizeModifier;
    }

    //Implementation method
    public void updateTailSizeModifier(float scale) {
        tailSizeModifier = scale;
    }

    //Implemented Abstract Method
    @Override
    public void drawGameObject() {
        sketch.fill(getColor()[0],getColor()[1],getColor()[2]);
        float[] screenPosition = SnakeGame.worldToScreenPosition(new int[] {getXPosition(), getYPosition()}, getMapSize());
        sketch.rect(screenPosition[0], screenPosition[1], 0.95f * getScale() * tailSizeModifier, 0.95f * getScale() * tailSizeModifier, 12);
    }
}
