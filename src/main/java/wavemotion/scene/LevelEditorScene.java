package wavemotion.scene;

import factories.SceneFactory;
import wavemotion.listeners.KeyListener;
import wavemotion.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene;
    private float timeToChangeScene;
    private Window window;

    public LevelEditorScene () {
        changingScene = false;
        timeToChangeScene = 2.0f;
        window = Window.getInstance();
    }

    @Override
    public void update (float deltaTime) {
        if (window != null) {
            if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
                changingScene = true;
            }

            if (changingScene && timeToChangeScene > 0.0f) {
                timeToChangeScene -= deltaTime;
                window.updateColor(deltaTime * -5.0f);
            } else if (changingScene) {
                window.changeScene(SceneFactory.SceneBaseType.Level);
            }
        }
    }
}
