package wavemotion;

import renderer.Texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class PickupTexture {
    private final int fboID;
    private final int pickingTextureID;
    private final int depthTexture;

    public PickupTexture(int width, int height) {
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        pickingTextureID = glGenTextures();
        glBindBuffer(GL_TEXTURE_2D, pickingTextureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, pickingTextureID, 0);

        // create depth buffer
        glEnable(GL_TEXTURE_2D);
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_ATTACHMENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        // disable the reading buffer
        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error: Framebuffer is not complete";
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void enableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboID);
    }

    public void disableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int x, int y) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboID);
        glReadBuffer(GL_COLOR_ATTACHMENT0);
        float[] pixels = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);
        return (int)pixels[0];
    }

    public static void bindShader() {

    }
}
