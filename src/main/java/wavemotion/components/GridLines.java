package wavemotion.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import utils.Settings;
import wavemotion.Window;

public class GridLines extends Component {
    @Override
    public void update(float dt) {
        Vector2f cameraPosition = Window.getCurrentScene().getCamera().position;
        Vector2f projectionSize = Window.getCurrentScene().getCamera().getProjectionSize();

        // TODO: consider adjusting these by  (firstx | firsty - 1) * GRID_SIZE if they end up looking 'off'
        int firstX = (int) ((cameraPosition.x / Settings.gridSize) - 1) * Settings.gridSize;
        int firstY = (int) ((cameraPosition.y / Settings.gridSize) - 1) * Settings.gridSize;

        // TODO: consider adjusting these by 2 if they end up looking 'off'
        int verticalLineCount = (int) (projectionSize.x / Settings.gridSize) + 2;
        int horizontalLineCount = (int) (projectionSize.y / Settings.gridSize)  + 2;

        // TODO: consider adjusting these by GRID_WIDTH * 2 if they end up looking 'off'
        int height = (int)projectionSize.y * 2;
        int width = (int)projectionSize.x * 2;

        int maxLines = Math.max(verticalLineCount, horizontalLineCount);
        Vector3f color = new Vector3f(0,0,0);
        for(int i = 0; i < maxLines; i++) {
            int x = firstX + Settings.gridSize * i;
            int y = firstY + Settings.gridSize * i;

            if(i < verticalLineCount) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if(i < horizontalLineCount) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);

            }
        }
    }
}
