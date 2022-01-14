package wavemotion.imgui;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import wavemotion.scene.Scene;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class ImGuiLayer {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long glfwWindow;

    public void init(long glfwWindow, String glslVersion) {
        this.glfwWindow = glfwWindow;
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/segoepr.ttf", 32, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more

        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    public void render(Scene currentScene) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        currentScene.sceneImGui();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public void destroyImGui() {
        imGuiGl3.dispose();
        imGuiGl3.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
    }
}
