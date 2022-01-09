import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wavemotion.Window;

public class WindowTest {
    private static final int DEFAULT_SCREEN_WIDTH = 1920;
    private static final int DEFAULT_SCREEN_HEIGHT = 1080;
    private Window window;

    @BeforeEach
    public void setUp() {
        window = Window.getInstance();
    }

    @Test
    @DisplayName("shouldCreateWindow")
    void shouldCreateWindow() {
        Assertions.assertNotNull(window);
    }

    @Test
    @DisplayName("validateDefaultWindowSize")
    void validateDefaultWindowSize() {
        Assertions.assertEquals(window.getWidth(), DEFAULT_SCREEN_WIDTH);
        Assertions.assertEquals(window.getHeight(), DEFAULT_SCREEN_HEIGHT);
    }

    @Test
    @DisplayName("glfwWindow should not be zero after init")
    void glfwWindowShouldNotBeZeroAfterInit() {
        window.init();
        Assertions.assertNotEquals(window.getGlfwWindow(), 0);
    }
}
