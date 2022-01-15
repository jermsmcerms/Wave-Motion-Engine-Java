package wavemotion.components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {
    private Texture texture = null;
    private float width;
    private float height;

    private Vector2f[] textureCoordinates = {
        new Vector2f(1,1),
        new Vector2f(1,0),
        new Vector2f(0,0),
        new Vector2f(0,1)
    };

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.textureCoordinates = texCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTextureId() {
        return texture == null ? -1 : texture.getId();
    }
}
