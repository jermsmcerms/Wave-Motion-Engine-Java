package wavemotion.scene;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private final String vertexShaderSrc =
        "layout (location = 0) in vec3 aPos;\n" +
        "layout (location = 1) in vec4 aColor;\n" +
        "out vec4 fColor;\n" +
        "void main() {\n" +
        "    fColor = aColor;\n" +
        "    gl_Position = vec4(aPos, 1.0);\n" +
        "}";

    private final String fragmentShaderSrc =
        "in vec4 fColor;\n" +
        "out vec4 color;\n" +
        "void main() {\n" +
        "    color = fColor;\n" +
        "}";

    private int vertexID;
    private int fragmentID;
    private int shaderProgramID;

    private float[] vertexArray = {
         0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, // bottom right
        -0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // top left
         0.5f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, // top right
        -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f  // bottom left
    };

    // must be counter clock-wise order
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    private int vaoId;
    private int vboId;
    private int eboId;

    public LevelEditorScene () {}

    @Override
    public void init() {
        // load & compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, length));

        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, length));

        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        if(glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tshader linking failed");
            System.out.println(glGetShaderInfoLog(shaderProgramID, length));
        }

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
        // Bind shader program
        glUseProgram(shaderProgramID);
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }
}
