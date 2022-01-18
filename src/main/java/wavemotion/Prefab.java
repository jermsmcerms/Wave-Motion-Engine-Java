package wavemotion;

import org.joml.Vector2f;
import wavemotion.components.Sprite;
import wavemotion.components.SpriteRenderer;
import wavemotion.components.Transform;
import wavemotion.entities.GameObject;

public class Prefab {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = new GameObject("sprite_obj_gen",
            new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
