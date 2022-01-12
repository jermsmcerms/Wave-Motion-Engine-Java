package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private String vertexSource;
    private String fragmentSource;
    private String path;
    private int shaderProgramID;
    private boolean isBeingUsed;

    public Shader(String path) {
        this.path = path;
        try {
            String source = new String(Files.readAllBytes(Paths.get(this.path)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            int sourceIndex = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", sourceIndex);
            String firstPattern = source.substring(sourceIndex, eol).trim();

            sourceIndex = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", sourceIndex);
            String secondPattern = source.substring(sourceIndex, eol).trim();

            if(firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token " + firstPattern);
            }

            if(secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token " + secondPattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isBeingUsed = false;
    }

    public void compileAndLink() {
        // load & compile vertex shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: "+ path +"\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, length));
        }

        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: "+ path +"\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        if(glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: "+ path +"\n\tshader linking failed");
            System.out.println(glGetShaderInfoLog(shaderProgramID, length));
        }
    }

    public void use() {
        if(!isBeingUsed) {
            glUseProgram(shaderProgramID);
            isBeingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        isBeingUsed = false;
    }

    public void uploadVec2f(String name, Vector2f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform2f(varLocation, vector.x, vector.y);
    }

    public void uploadVec3f(String name, Vector3f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform3f(varLocation, vector.x, vector.y, vector.z);
    }

    public void uploadVec4f(String name, Vector4f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform4f(varLocation, vector.x, vector.y, vector.z, vector.w);
    }

    public void uploadMat3f(String name, Matrix3f matrix4f) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        matrix4f.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadMat4f(String name, Matrix4f matrix4f) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadFloat(String name, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String name, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(varLocation, value);
    }
}
