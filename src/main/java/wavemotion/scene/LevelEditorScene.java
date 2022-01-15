package wavemotion.scene;

import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Renderer;
import utils.AssetPool;
import wavemotion.Camera;
import wavemotion.Prefab;
import wavemotion.components.*;
import wavemotion.entities.GameObject;
import wavemotion.listeners.MouseListener;

import java.security.InvalidKeyException;
import java.util.ArrayList;

public class LevelEditorScene extends Scene {
    private static final String spriteSheetPath = "assets/images/spritesheets/decorationsAndBlocks.png";
    private SpriteSheet spriteSheet;
    private MouseControls mouseControls;

    public LevelEditorScene () {
        gameObjectList = new ArrayList<>();
        mouseControls = new MouseControls();
    }

    @Override
    public void init() {
        loadResources();
        renderer = new Renderer();
        camera = new Camera(new Vector2f());
        try {
            spriteSheet = AssetPool.getSpriteSheet(spriteSheetPath);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if(levelLoaded) {
            activeGameObject = gameObjectList.get(0);
        } else {
            GameObject red = new GameObject("image1",
                new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), -1);
            SpriteRenderer spriteRenderer = new SpriteRenderer();
            spriteRenderer.setColor(new Vector4f(1, 0, 0, 1));
            red.addComponent(spriteRenderer);
            addGameObjectToScene(red);
            activeGameObject = red;

            GameObject green = new GameObject("image2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
            SpriteRenderer spriteRenderer1 = new SpriteRenderer();
            Sprite sprite = new Sprite();
            sprite.setTexture(AssetPool.getTexture("assets/images/green.png"));
            spriteRenderer1.setSprite(sprite);
            green.addComponent(spriteRenderer1);
            addGameObjectToScene(green);
        }
    }

    @Override
    public void update (float deltaTime) {
        mouseControls.update(deltaTime);
        for(GameObject go : gameObjectList) {
            go.update(deltaTime);
        }

        renderer.render();
    }

    @Override
    public void imGui() {
        ImGui.begin("Sprite Palette");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 getWindowSize = new ImVec2();
        ImGui.getWindowSize(getWindowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + getWindowSize.x;
        for(int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float width = sprite.getWidth() * 4;
            float height = sprite.getHeight() * 4;
            int id = sprite.getTextureId();
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, width, height, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject gameObject = Prefab.generateSpriteObject(sprite, width, height);
                mouseControls.pickupObject(gameObject);
            }
            ImGui.popID();

            ImVec2 lastPos = new ImVec2();
            ImGui.getItemRectMax(lastPos);
            float lastButtonX2 = lastPos.x;
            float nextButtonX2 = lastPos.x + itemSpacing.x + width;

            if(i+1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/defaultShader.glsl");

        AssetPool.addSpriteSheet(spriteSheetPath,
            new SpriteSheet(AssetPool.getTexture(spriteSheetPath), 16, 16, 81, 0));
        AssetPool.getTexture("assets/images/green.png");
    }
}
