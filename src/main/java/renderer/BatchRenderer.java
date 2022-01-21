package renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import utils.AssetPool;
import wavemotion.Window;
import wavemotion.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer implements Comparable<BatchRenderer> {
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private List<Texture> textureList;
    private int numSprites;
    private boolean isFull;
    private float[] vertices;
    private int vaoId;
    private int vboId;
    private int maxBatchSize;
    private int[] textureSlots = {0,1,2,3,4,5,6,7};
    private int zIndex;

    public BatchRenderer(int maxBatchSize, int zIndex) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;
        this.sprites = new SpriteRenderer[this.maxBatchSize];
        // 4 is the number of vertices per quadrilateral
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        numSprites = 0;
        isFull = false;
        textureList = new ArrayList<>();
    }

    public int getZIndex() {
        return zIndex;
    }

    public void start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboId = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer spriteRenderer) {
        int index = numSprites;
        sprites[index] = spriteRenderer;
        numSprites++;

        if(spriteRenderer.getTexture() != null) {
            if(!textureList.contains(spriteRenderer.getTexture())) {
                textureList.add(spriteRenderer.getTexture());
            }
        }

        loadVertexProperties(index);
        if(numSprites >= maxBatchSize) {
            isFull = true;
        }
    }

    public void render() {
        boolean rebufferData = false;
        for(int i = 0; i < numSprites; i++) {
            SpriteRenderer spriteRenderer = sprites[i];
            if(spriteRenderer.isDirty()) {
                rebufferData = true;
                loadVertexProperties(i);
                spriteRenderer.clean();
            }
        }

        if(rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        Shader shader = Renderer.getBoundShader();
        shader.uploadMat4f("uProjMatrix", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uViewMatrix", Window.getCurrentScene().getCamera().getViewMatrix());
        for(int i = 0; i < textureList.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textureList.get(i).bind();
        }

        shader.uploadIntArray("uTextures", textureSlots);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (Texture texture : textureList) {
            texture.unbind();
        }

        shader.detach();
    }


    public boolean isFull() {
        return isFull;
    }

    public boolean isTextureSlotsFull() {
        return textureList.size() < textureSlots.length;
    }

    public boolean hasTexture(Texture texture) {
        return textureList.contains(texture);
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = Float.BYTES * index;
        elements[offsetArrayIndex]      = offset + 3;
        elements[offsetArrayIndex + 1]  = offset + 2;
        elements[offsetArrayIndex + 2]  = offset;
        elements[offsetArrayIndex + 3]  = offset;
        elements[offsetArrayIndex + 4]  = offset + 2;
        elements[offsetArrayIndex + 5]  = offset + 1;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = sprites[index];
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] textCoords = sprite.getTextureCoordinates();

        int textureId = 0;
        if(sprite.getTexture() != null) {
            for (int i = 0; i < textureList.size(); i++) {
                if(textureList.get(i).equals(sprite.getTexture())) {
                    textureId = i + 1;
                    break;
                }
            }
        }
        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for(int i = 0; i < 4; i++) {
            if(i == 1) {
                yAdd = 0.0f;
            } else if(i == 2) {
                xAdd = 0.0f;
            } else if(i == 3) {
                yAdd = 1.0f;
            }

            vertices[offset] = sprite.parent.transform.position.x + (xAdd * sprite.parent.transform.scale.x);
            vertices[offset+1] = sprite.parent.transform.position.y + (yAdd * sprite.parent.transform.scale.y);

            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            vertices[offset+6] = textCoords[i].x;
            vertices[offset+7] = textCoords[i].y;

            vertices[offset+8] = textureId;

            vertices[offset + 9] = sprite.parent.getUID() + 1;

            offset += VERTEX_SIZE;
        }
    }

    @Override
    public int compareTo(BatchRenderer o) {
        return Integer.compare(zIndex, o.zIndex);
    }
}
