package pixelDrawers;

import java.awt.*;

public class GraphicsPixelDrawer implements PixelDrawer {

    private Graphics graphics;

    public GraphicsPixelDrawer(Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void drawPixel(int x, int y, Color color) {
        graphics.setColor(color);
        graphics.fillRect(x, y, 1, 1);
    }

}
