package wavemotion;

import factories.SceneFactory;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import wavemotion.listeners.KeyListener;
import wavemotion.listeners.MouseListener;
import wavemotion.scene.Scene;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final int width;
    private final int height;
    private final String title;
    private static Window window = null;
    private static Scene currentScene;
    private long glfwWindow;


    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Wave Motion Engine";
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static Window getInstance() {
        if(Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static void changeScene(SceneFactory.SceneBaseType sceneType) {
        currentScene = SceneFactory.makeScene(sceneType);
        assert currentScene != null;
        currentScene.init();
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public void run() {
        String welcomeMsg = "Welcome to the Wave Motion Engine! V:";
        System.out.println(welcomeMsg + Version.getVersion());

        init();
        runLoop();

        // clean up
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        // set up error callbacks
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW
        String errorMsg = "";
        if(!glfwInit()) {
            errorMsg = "Unable to initialize GLFW";
            throw new IllegalStateException(errorMsg);
        }

        // GLFW configuration
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            errorMsg = "Failed to create glfw window";
            throw new IllegalStateException(errorMsg);
        }

        // configure mouse listener
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyListenerCallback);

        // Make OpenGL context
        glfwMakeContextCurrent(glfwWindow);
        // enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // From GLFW: This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(SceneFactory.SceneBaseType.LevelEditor);
    }

    public void runLoop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = 1.0f;

        while(!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0.0f) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
