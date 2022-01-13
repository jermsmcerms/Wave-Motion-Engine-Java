package renderer;

import wavemotion.components.SpriteRenderer;
import wavemotion.entities.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final int MAX_BATCH_SIZE = 1000;

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
        for(BatchRenderer batch : batches) {
            batch.render();
        }
    }

    private void add(SpriteRenderer spriteRenderer) {
        boolean added = false;
        for(BatchRenderer batch : batches) {
            if(!batch.isFull()) {
                Texture texture = spriteRenderer.getTexture();
                if(texture == null || (batch.hasTexture(texture) || batch.isTextureSlotsFull())) {
                    batch.addSprite(spriteRenderer);
                    added = true;
                    break;
                }
            }
        }

        if(!added) {
            BatchRenderer batchRenderer = new BatchRenderer(MAX_BATCH_SIZE);
            batchRenderer.start();
            batches.add(batchRenderer);
            batchRenderer.addSprite(spriteRenderer);
        }
    }
}
