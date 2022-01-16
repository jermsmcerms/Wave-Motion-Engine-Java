package wavemotion.components;

import utils.Settings;
import wavemotion.Window;
import wavemotion.entities.GameObject;
import wavemotion.listeners.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject pickupObject) {
        holdingObject = pickupObject;
        Window.getCurrentScene().addGameObjectToScene(holdingObject);
    }

    public void placeObject() {
        System.out.println("placing object");
        holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.gridWidth) * Settings.gridWidth;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.gridHeight) * Settings.gridHeight;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject();
            }
        }
    }
}
