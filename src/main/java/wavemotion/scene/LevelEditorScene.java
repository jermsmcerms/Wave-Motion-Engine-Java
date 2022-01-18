package wavemotion.scene;

import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import renderer.Renderer;
import utils.AssetPool;
import wavemotion.Camera;
import wavemotion.Prefab;
import wavemotion.components.*;
import wavemotion.entities.GameObject;

import java.security.InvalidKeyException;
import java.util.ArrayList;

public class LevelEditorScene extends Scene {
    private static final String spriteSheetPath = "assets/images/spritesheets/decorationsAndBlocks.png";
    private SpriteSheet spriteSheet;
    private GameObject levelEditorRoot = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene () {
        gameObjectList = new ArrayList<>();
    }

    @Override
    public void init() {
        levelEditorRoot.addComponent(new MouseControls());
        levelEditorRoot.addComponent(new GridLines());
        loadResources();
        renderer = new Renderer();
        camera = new Camera(new Vector2f());
        try {
            spriteSheet = AssetPool.getSpriteSheet(spriteSheetPath);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        DebugDraw.addLine2D(new Vector2f(0,0), new Vector2f(800, 800), new Vector3f(0,0,1), 120);

        if(levelLoaded && gameObjectList.size() > 0) {
            activeGameObject = gameObjectList.get(0);
        }
    }

    @Override
    public void update (float deltaTime) {
        levelEditorRoot.update(deltaTime);
        for(GameObject go : gameObjectList) {
            go.update(deltaTime);
        }
    }

    @Override
    public void render() {
        renderer.render();
    }

    @Override
    public void imGui() {
        ImGui.begin("Sprite Palette");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float width = sprite.getWidth() * 2;
            float height = sprite.getHeight() * 2;
            int id = sprite.getTextureId();
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, width, height, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject gameObject = Prefab.generateSpriteObject(sprite, width, height);
                levelEditorRoot.getComponent(MouseControls.class).pickupObject(gameObject);
            }
            ImGui.popID();

            ImVec2 lastPos = new ImVec2();
            ImGui.getItemRectMax(lastPos);
            float nextButtonX2 = lastPos.x + itemSpacing.x + width;

            if(i+1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }

    private void loadResources() {
//        AssetPool.getShader("assets/shaders/defaultShader.glsl");
//
        AssetPool.addSpriteSheet(spriteSheetPath,
            new SpriteSheet(AssetPool.getTexture(spriteSheetPath), 16, 16, 81, 0));
//        AssetPool.getTexture("assets/images/green.png");

        for(GameObject go : gameObjectList) {
            if(go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRenderer = go.getComponent(SpriteRenderer.class);
                if(spriteRenderer.getTexture() != null) {
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getPath()));
                }
            }
        }
    }
}
