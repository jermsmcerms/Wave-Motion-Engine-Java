package wavemotion.components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> spriteSheet;

    public SpriteSheet(Texture texture, int width, int height, int numSprites, int padding) {
        this.texture = texture;
        spriteSheet = new ArrayList<>();

        int currentX = 0;
        int currentY = texture.getHeight() - height;
        for(int i = 0; i < numSprites; i++) {
            float topY = (currentY + height) / (float)texture.getHeight();
            float rightX = (currentX + width) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords =
                new Vector2f[] {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
                };
            Sprite sprite = new Sprite(texture, texCoords);
            spriteSheet.add(sprite);
            currentX += width + padding;
            if(currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= height + padding;
            }
        }
    }

    public Sprite getSprite(int index) {
        return spriteSheet.get(index);
    }
}
