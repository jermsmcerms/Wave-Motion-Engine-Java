package wavemotion.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static final int NUM_MOUSE_BUTTONS = 3;
    private static MouseListener mouseListener;
    private double scrollX;
    private double scrollY;
    private double xPos;
    private double yPos;
    private double lastX;
    private double lastY;
    private boolean mouseButtonClicked[] = new boolean[NUM_MOUSE_BUTTONS];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos    = 0.0;
        this.yPos    = 0.0;
        this.lastX   = 0.0;
        this.lastY   = 0.0;
        this.isDragging = false;
    }

    public static MouseListener getInstance() {
        if(MouseListener.mouseListener == null) {
            MouseListener.mouseListener = new MouseListener();
        }
        return MouseListener.mouseListener;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;

        for(int i = 0; i < getInstance().mouseButtonClicked.length; i++) {
            if(getInstance().mouseButtonClicked[i]) {
                getInstance().isDragging = true;
                break;
            }
        }
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if(button < 0 || button >= getInstance().mouseButtonClicked.length) {
            String errorMessage = "key: " + button +
                    "out of bounds for length: " + getInstance().mouseButtonClicked.length;
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }

        if(action == GLFW_PRESS) {
                    getInstance().mouseButtonClicked[button] = true;
            } else if(action == GLFW_RELEASE) {
            getInstance().mouseButtonClicked[button] = false;
            getInstance().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame() {
        getInstance().scrollX = 0.0;;
        getInstance().scrollY = 0.0;
        getInstance().xPos    = 0.0;
        getInstance().yPos    = 0.0;
        getInstance().lastX   = 0.0;
        getInstance().lastY   = 0.0;
    }

    public static float getX() {
        return (float)getInstance().xPos;
    }

    public static float getY() {
        return (float)getInstance().yPos;
    }

    public static float getDx() {
        return (float)(getInstance().lastX - getInstance().xPos);
    }

    public static float getDy() {
        return (float)(getInstance().lastY - getInstance().yPos);
    }

    public static float getScrollX() {
        return (float)getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float)getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if(button < 0 || button >= getInstance().mouseButtonClicked.length) {
            String errorMessage = "key: " + button +
                "out of bounds for length: " + getInstance().mouseButtonClicked.length;
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }
        return getInstance().mouseButtonClicked[button];
    }
}
