package renderer;

import wavemotion.components.SpriteRenderer;
import wavemotion.entities.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private static final int MAX_BATCH_SIZE = 1000;
    private static Shader currentShader;

    private List<BatchRenderer> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if(spriteRenderer != null) {
            add(spriteRenderer);
        }
    }

    public void render() {
        currentShader.use();
        for(BatchRenderer batch : batches) {
            batch.render();
        }
    }

    private void add(SpriteRenderer spriteRenderer) {
        boolean added = false;
        for(BatchRenderer batch : batches) {
            if(!batch.isFull() && spriteRenderer.parent.getZIndex() == batch.getZIndex()) {
                Texture texture = spriteRenderer.getTexture();
                if(texture == null || (batch.hasTexture(texture) || batch.isTextureSlotsFull())) {
                    batch.addSprite(spriteRenderer);
                    added = true;
                    break;
                }
            }
        }

        if(!added) {
            BatchRenderer batchRenderer = new BatchRenderer(MAX_BATCH_SIZE, spriteRenderer.parent.getZIndex());
            batchRenderer.start();
            batches.add(batchRenderer);
            batchRenderer.addSprite(spriteRenderer);
            Collections.sort(batches);
        }
    }

    public static void bindShader(Shader shader) {
        currentShader = shader;
    }

    public static Shader getBoundShader() {
        return currentShader;
    }
}
