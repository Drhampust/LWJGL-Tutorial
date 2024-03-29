package engine.maths;

public class Vector2f {
    private float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2f() {
        this.x = 0f;
        this.y = 0f;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
