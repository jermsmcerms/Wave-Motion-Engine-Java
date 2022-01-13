package wavemotion.scene;

import org.joml.Vector2f;
import renderer.Renderer;
import renderer.Texture;
import utils.AssetPool;
import wavemotion.Camera;
import wavemotion.components.Sprite;
import wavemotion.components.SpriteRenderer;
import wavemotion.components.SpriteSheet;
import wavemotion.components.Transform;
import wavemotion.entities.GameObject;

import java.security.InvalidKeyException;
import java.util.ArrayList;

public class LevelEditorScene extends Scene {
    private static final String spriteSheetPath = "assets/images/spritesheet.png";

    public LevelEditorScene () {
        gameObjectList = new ArrayList<>();
    }

    @Override
    public void init() {
        loadResources();
        renderer = new Renderer();
        camera = new Camera(new Vector2f());

        try {
            SpriteSheet spriteSheet = AssetPool.getSpriteSheet(spriteSheetPath);

            GameObject red = new GameObject("image1",
                new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 13);
            red.addComponent(new SpriteRenderer(
                new Sprite(AssetPool.getTexture("assets/images/red.png"))
            ));
            addGameObjectsToScene(red);

            GameObject green = new GameObject("image2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 10);
            green.addComponent(new SpriteRenderer(
                new Sprite(AssetPool.getTexture("assets/images/green.png"))
            ));
            addGameObjectsToScene(green);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update (float deltaTime) {
        for(GameObject go : gameObjectList) {
            go.update(deltaTime);
        }

        renderer.render();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/defaultShader.glsl");
        AssetPool.addSpriteSheet(spriteSheetPath,
            new SpriteSheet(AssetPool.getTexture(spriteSheetPath), 16, 16, 26, 0));
    }
}
