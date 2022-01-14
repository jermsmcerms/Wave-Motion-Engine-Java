package wavemotion.components;

import wavemotion.entities.GameObject;

public abstract class Component {
    public GameObject parent = null;
    public void start() {}
    public void update(float dt) {}
    public void imGui() {}
}
