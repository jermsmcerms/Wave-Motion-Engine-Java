package wavemotion;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static final int NUM_KEYS = 350;
    private static KeyListener keyListener;
    private boolean[] keyPressed = new boolean[NUM_KEYS];

    public static KeyListener getInstance() {
        if(keyListener == null) {
            keyListener = new KeyListener();
        }
        return keyListener;
    }

    public static void keyListenerCallback(long window, int key, int scanCode, int action, int mods) {
        if(key < 0 || key >= getInstance().keyPressed.length) {
            String errorMessage = "key: " + key + "out of bounds for length: " + getInstance().keyPressed.length;
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }

        if(action == GLFW_PRESS) {
            getInstance().keyPressed[key] = true;
        } else if(action == GLFW_RELEASE) {
            getInstance().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        if(key < 0 || key >= getInstance().keyPressed.length) {
            String errorMessage = "key: " + key + "out of bounds for length: " + getInstance().keyPressed.length;
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }
        return getInstance().keyPressed[key];
    }
}
