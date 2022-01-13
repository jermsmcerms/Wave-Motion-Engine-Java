package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int width;
    private final int height;
    private String path;
    private int textureId;

    public Texture(String path) {
        this.path = path;
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        // repeat textures if coordinates are larger than the texture image
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // use pixelate mode to interpolate stretched/shrunk textures
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer bufferWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bufferHeight = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer image = stbi_load(this.path, bufferWidth, bufferHeight, channels, 0);
        if(image != null) {
            width = bufferWidth.get(0);
            height = bufferHeight.get(0);
            int colorFormat = 0;
            if(channels.get(0) == 3) {
                colorFormat = GL_RGB;
            } else if(channels.get(0) == 4) {
                colorFormat = GL_RGBA;
            } else {
                throw new IllegalArgumentException("Unknown channel value: " + channels.get(0));
            }
            glTexImage2D(GL_TEXTURE_2D, 0, colorFormat, bufferWidth.get(0), bufferHeight.get(0),
                    0, colorFormat, GL_UNSIGNED_BYTE, image);
        } else {
            throw new NullPointerException("Error loading image texture: " + this.path);
        }

        // Does not use JVM garbage collector. So, must be freed manually.
        stbi_image_free(image);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
