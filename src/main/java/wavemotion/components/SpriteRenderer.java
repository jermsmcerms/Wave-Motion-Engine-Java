package wavemotion.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4f color;
    private Sprite sprite;
    private Transform lastTransform;
    private boolean isDirty;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        sprite = new Sprite(null);
        isDirty = false;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        color = new Vector4f(1.0f,1.0f,1.0f,1.0f);
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        if(!this.color.equals(color)) {
            this.color.set(color);
            isDirty = true;
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    public Vector2f[] getTextureCoordinates() {
        return sprite.getTextureCoordinates();
    }


    public boolean isDirty() {
        return isDirty;
    }

    public void clean() {
        isDirty = false;
    }

    @Override
    public void start() {
        lastTransform = parent.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!lastTransform.equals(parent.transform)) {
            parent.transform.copyTransform(lastTransform);
            isDirty = true;
        }
    }
}
