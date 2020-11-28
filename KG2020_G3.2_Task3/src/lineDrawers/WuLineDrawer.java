package lineDrawers;

import pixelDrawers.PixelDrawer;
import utils.ScreenPoint;

import java.awt.*;

public class WuLineDrawer implements LineDrawer {

    private PixelDrawer pixelDrawer;

    public WuLineDrawer(PixelDrawer pixelDrawer) {
        this.pixelDrawer = pixelDrawer;
    }

    private float fractionalPart(float number) {
        return Math.abs(number) - (int) Math.abs(number);
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        int red = color.getRed();
        int blue = color.getBlue();
        int green = color.getGreen();

        float gradient;
        if (absDx > absDy) {
            if (x1 > x2) {
                x1 = swap(x2, x2 = x1);
                y1 = swap(y2, y2 = y1);
            }
            gradient = (float) dy / dx;
            float interY = y1 + gradient;
            pixelDrawer.drawPixel(x1, y1, color);
            for (int x = x1; x < x2; x++) {
                int alpha = (int) Math.abs(255 - fractionalPart(interY) * 255);
                int invertedAlpha = (int) Math.abs(fractionalPart(interY) * 255);
                Color color1 = new Color(red, green, blue, alpha);
                Color color2 = new Color(red, green, blue, invertedAlpha);
                pixelDrawer.drawPixel(x, (int)interY, color1);
                pixelDrawer.drawPixel(x, (int)interY + 1, color2);
                interY += gradient;
            }
        }
        else {
            if (y1 > y2) {
                x1 = swap(x2, x2 = x1);
                y1 = swap(y2, y2 = y1);
            }
            gradient = (float) dx / dy;
            float interX = x1 + gradient;
            pixelDrawer.drawPixel(x1, y1, color);
            for (int y = y1; y < y2; y++) {
                int alpha = (int) (255 - fractionalPart(interX) * 255);
                int invertedAlpha = (int) (fractionalPart(interX) * 255);
                Color color1 = new Color(red, green, blue, alpha);
                Color color2 = new Color(red, green, blue, invertedAlpha);
                pixelDrawer.drawPixel((int)interX, y, color1);
                pixelDrawer.drawPixel((int)interX + 1, y, color2);
                interX += gradient;
            }
        }
        pixelDrawer.drawPixel(x2, y2, color);
    }

    @Override
    public void drawLine(ScreenPoint point1, ScreenPoint point2, Color color) {
        drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY(), color);
    }

}
