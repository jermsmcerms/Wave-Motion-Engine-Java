package wavemotion.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import renderer.Renderer;
import utils.ComponentDeserializer;
import utils.GameObjectDeserializer;
import wavemotion.Camera;
import wavemotion.components.Component;
import wavemotion.entities.GameObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    protected List<GameObject> gameObjectList;
    protected boolean isRunning = false;
    protected Renderer renderer;
    protected GameObject activeGameObject;
    protected boolean levelLoaded = false;

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
    public void addGameObjectToScene(GameObject go) {
        if(!isRunning) {
            gameObjectList.add(go);
        } else {
            gameObjectList.add(go);
            go.start();
            renderer.add(go);
        }
    }

    public void sceneImGui() {
        if(activeGameObject != null) {
            ImGui.begin("inspector");
            activeGameObject.imGui();
            ImGui.end();
        }

        imGui();
    }

    public void imGui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component.class, new ComponentDeserializer())
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
            .create();

        try {
            FileWriter fw = new FileWriter("level.txt");
            fw.write(gson.toJson(gameObjectList));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component.class, new ComponentDeserializer())
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
            .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);
                for(Component component : objs[i].getAllComponents()) {
                    if(component.getUID() > maxCompId) {
                        maxCompId = component.getUID();
                    }
                }
                if(objs[i].getUID() > maxGoId) {
                    maxGoId = objs[i].getUID();
                }
            }

            maxGoId++;
            maxCompId++;
            System.out.println(maxGoId + " " + maxCompId);
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.levelLoaded = true;
        }
    }

    public abstract void update(float deltaTime);
}