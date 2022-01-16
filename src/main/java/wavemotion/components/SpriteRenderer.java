package wavemotion.components;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();
    private transient Transform lastTransform;
    private transient boolean isDirty = true;

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

    public void setTexture(Texture texture) {
        sprite.setTexture(texture);
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

    @Override
    public void imGui() {
        float[] imColors = { color.x, color.y, color.z, color.w };
        if(ImGui.colorPicker4("Color Picker: ", imColors)) {
            this.color.set(imColors);
            isDirty = true;
        }
    }
}
