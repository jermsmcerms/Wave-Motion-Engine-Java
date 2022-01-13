package wavemotion;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        setToOrtho();
    }

    public void setToOrtho() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f forwardVector = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f upVector = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
            forwardVector.add(position.x, position.y, 0.0f), upVector);

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}