package factories;

import wavemotion.scene.LevelEditorScene;
import wavemotion.scene.LevelScene;
import wavemotion.scene.Scene;

public class SceneFactory {
    public enum SceneBaseType {
        Level, LevelEditor
    }
    public static Scene makeScene(SceneBaseType type) {
        switch(type) {
            case Level:
                return new LevelScene();
            case LevelEditor:
                return new LevelEditorScene();
            default:
                return null;
        }
    }
}
