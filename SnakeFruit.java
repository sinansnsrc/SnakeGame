import processing.core.PApplet;

public class SnakeFruit extends GameObject {
    //Fields
    private float fruitAnimationScale;
    private boolean isGrowing;
    private float fruitRotation;

    //Constructor
    public SnakeFruit(int[] arrayPosition, int[] color, PApplet sketch, int mapSize) {
        super(arrayPosition, color, sketch, mapSize);
        fruitAnimationScale = 1.0f;
        isGrowing = false;
        fruitRotation = 0;
    }

    //Implemented Abstract Method
    @Override
    public void drawGameObject() {
        if(fruitAnimationScale < 1.0f && isGrowing) {
            fruitAnimationScale += 0.005;
        }
        else if (fruitAnimationScale > 0.75f && !isGrowing) {
            fruitAnimationScale -= 0.005f;
        }
        else {
            isGrowing = !isGrowing;
        }

        float[] screenPosition = SnakeGame.worldToScreenPosition(new int[] {getXPosition(), getYPosition()}, getMapSize());

        sketch.pushMatrix();

        fruitRotation += 0.5;
        sketch.translate(screenPosition[0],screenPosition[1]);
        sketch.rotate(sketch.radians(fruitRotation));

        sketch.fill(getColor()[0],getColor()[1],getColor()[2]);
        sketch.rect(0 + getScale() / 4, 0, 0.45f * getScale() * fruitAnimationScale, 0.45f * getScale() * fruitAnimationScale,8);
        sketch.rect(0 - getScale() / 4, 0, 0.45f * getScale() * fruitAnimationScale, 0.45f * getScale() * fruitAnimationScale,8);
        sketch.rect(0, 0 + getScale() / 4, 0.45f * getScale() * fruitAnimationScale, 0.45f * getScale() * fruitAnimationScale,8);
        sketch.rect(0, 0 - getScale() / 4, 0.45f * getScale() * fruitAnimationScale, 0.45f * getScale() * fruitAnimationScale,8);

        sketch.popMatrix();
    }
}
