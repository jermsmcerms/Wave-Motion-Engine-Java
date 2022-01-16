package renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {
    private final Vector2f start;
    private final Vector2f end;
    private final Vector3f color;
    private int duration;

    public Line2D(Vector2f start, Vector2f end, Vector3f color, int duration) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.duration = duration;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector3f getColor() {
        return color;
    }

    public int beginFrame() {
        duration--;
        return duration;
    }
}
