package wavemotion.components;

import org.joml.Vector2f;

public class Transform extends Component {
    public Vector2f position;
    public Vector2f scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(position), new Vector2f(scale));
    }

    public void copyTransform(Transform copyTransform) {
        copyTransform.position.set(position);
        copyTransform.scale.set(scale);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }

        if(!(o instanceof Transform)) {
            return false;
        }

        Transform t = (Transform) o;
        return t.position.equals(position) && t.scale.equals(scale);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }
}
