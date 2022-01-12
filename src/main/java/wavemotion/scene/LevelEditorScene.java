package wavemotion.scene;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import utils.Time;
import wavemotion.Camera;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private Shader defaultShader;

    private float[] vertexArray = {
        100.5f,   0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 0.0f, // bottom right
          0.5f, 100.5f, 0.0f,     0.0f, 1.0f, 0.0f, 0.0f, // top left
        100.5f, 100.5f, 0.0f,     0.0f, 0.0f, 1.0f, 0.0f, // top right
          0.5f,   0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 0.0f  // bottom left
    };

    // must be counter clock-wise order
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    private int vaoId;
    private int vboId;
    private int eboId;

    public LevelEditorScene () {
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/defaultShader.glsl");
        defaultShader.compileAndLink();

        // create VAO, VBO, and EBO buffers
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeInBytes = (positionSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update (float deltaTime) {
        camera.position.x -= deltaTime * 50.0f;
        camera.position.y -= deltaTime * 50.0f;
        // Bind shader program
        defaultShader.use();

        defaultShader.uploadMat4f("uProjMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();
    }
}
