package lineDrawers;

import utils.ScreenPoint;

import java.awt.*;

public interface LineDrawer {
    void drawLine(int x1, int y1, int x2, int y2, Color color);

    default int countStep(int delta) {
        return (delta > 0) ? 1 : -1;
    }

    void drawLine(ScreenPoint point1, ScreenPoint point2, Color color);

    default int swap(int first, int second) {
        return first;
    }

}
