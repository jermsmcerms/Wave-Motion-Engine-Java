package wavemotion.entities;

import wavemotion.components.Component;
import wavemotion.components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> componentsList;
    public Transform transform;

    public GameObject(String name) {
        this(name, new Transform());
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        componentsList = new ArrayList<>();
        this.transform = transform;
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
