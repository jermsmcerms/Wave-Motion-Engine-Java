package wavemotion.scene;

import renderer.Renderer;
import wavemotion.Camera;
import wavemotion.entities.GameObject;

import java.util.List;

public abstract class Scene {
    protected Camera camera;
    protected List<GameObject> gameObjectList;
    protected boolean isRunning = false;
    protected Renderer renderer;

    public Scene() {
    }

    public void init() {

    }

    public Camera getCamera() {
        return camera;
    }

    public void start() {
        for(GameObject go : gameObjectList) {
            go.start();
            renderer.add(go);
        }
        isRunning = true;
    }
    public void addGameObjectsToScene(GameObject go) {
        if(!isRunning) {
            gameObjectList.add(go);
        } else {
            gameObjectList.add(go);
            go.start();
            renderer.add(go);
        }
    }

    public abstract void update(float deltaTime);
}