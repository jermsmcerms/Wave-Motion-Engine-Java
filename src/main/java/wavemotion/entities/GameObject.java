package wavemotion.entities;

import wavemotion.components.Component;
import wavemotion.components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public Transform transform;
    private String name;
    private List<Component> componentsList;
    private int zIndex;

    public GameObject(String name) {
        this(name, new Transform(), 0);
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        componentsList = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public int getZIndex() {
        return zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component component : componentsList) {
            if(componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public void start() {
        for (int i = 0; i < componentsList.size(); i++) {
            componentsList.get(i).start();
        }
    }

    public void update(float dt) {
        for(int i = 0; i < componentsList.size(); i++) {
            componentsList.get(i).update(dt);
        }
    }

    public void addComponent(Component component) {
        componentsList.add(component);
        component.parent = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(int i = 0; i < componentsList.size(); i++) {
            if(componentClass.isAssignableFrom(componentsList.get(i).getClass())) {
                componentsList.remove(i);
                return;
            }
        }
    }
}
