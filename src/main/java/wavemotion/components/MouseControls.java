package wavemotion.components;

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
        holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if(holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject();
            }
        }
    }
}
