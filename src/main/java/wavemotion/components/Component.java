package wavemotion.components;

import imgui.ImGui;
import wavemotion.entities.GameObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    public transient GameObject parent = null;

    private static int ID_COUNT = 0;
    private int uid = -1;

    public void start() {}
    public void update(float dt) {}

    public void imGui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields) {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if(isPrivate) {
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object object = field.get(this);
                String name = field.getName();
                if(type == int.class) {
                    int value = (int) object;
                    int[] imValues = {value};
                    if(ImGui.dragInt(name + ": ", imValues)) {
                        field.set(this, imValues[0]);
                    }
                } else if(type == float.class) {
                    float value = (float) object;
                    float[] imValues = {value};
                    if(ImGui.dragFloat(name + ": ", imValues)) {
                        field.set(this, imValues[0]);
                    }
                }
                if(isPrivate) {
                    field.setAccessible(false);
                }
            }

        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void generateId() {
        if(uid == -1) {
            uid = ID_COUNT++;
        }
    }

    public int getUID() {
        return uid;
    }

    public static void init(int maxID) {
        ID_COUNT = maxID;
    }
}
