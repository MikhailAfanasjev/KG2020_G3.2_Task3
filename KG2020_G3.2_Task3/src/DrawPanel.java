import lineDrawers.LineDrawer;
import lineDrawers.WuLineDrawer;
import pixelDrawers.BufferedImagePixelDrawer;
import pixelDrawers.PixelDrawer;
import utils.Line;
import utils.RealPoint;
import utils.ScreenConverter;
import utils.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;


public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private ScreenConverter screenConverter = new ScreenConverter(
            -2, 2, 4, 4, getWidth(), getHeight());
    private ScreenPoint lastPosition = null;
    private List<Line> allLines = new LinkedList<>();
    private Line currentNewLine = null;
    private Line MyLine = null;
    private PixelDrawer pixelDrawer = null;
    private LineDrawer lineDrawer = null;
    ScreenPoint cp = null;

    public DrawPanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);

    }

    @Override
    public void paint(Graphics g) {
        screenConverter.setScreenWidth(getWidth());
        screenConverter.setScreenHeight(getHeight());
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        pixelDrawer = new BufferedImagePixelDrawer(bufferedImage);
        lineDrawer = new WuLineDrawer(pixelDrawer);
        Graphics buffGraphics = bufferedImage.createGraphics();
        buffGraphics.setColor(Color.LIGHT_GRAY);
        buffGraphics.fillRect(0, 0, getWidth(), getHeight());
        drawAxis(lineDrawer);

        for (Line line : allLines) {
            drawLine(lineDrawer, line);
        }
        if (currentNewLine != null) drawLine(lineDrawer, currentNewLine);
        if (MyLine != null) drawLine(lineDrawer, MyLine);
        g.drawImage(bufferedImage, 0, 0, null);
        buffGraphics.dispose();

    }

    private void drawLine(LineDrawer lineDrawer, Line line) {


        lineDrawer.drawLine(
                screenConverter.realToScreen(line.getP1()),
                screenConverter.realToScreen(line.getP2()),
                Color.BLACK);

        Point2D p = new Point2D.Double(line.getP2().getX(), line.getP2().getY());
        double ax = line.getP2().getX();
        double ay = line.getP2().getY();

        double dx = line.getP2().getX() - line.getP1().getX(), dy = line.getP2().getY() - line.getP1().getY();
        double angle = Math.atan2(dy, dx);
        RealPoint rp = new RealPoint(0.2 * Math.cos(angle-2.5) + ax, 0.2 * Math.sin(angle-2.5) + ay);
        RealPoint rp2 = new RealPoint(0.2 * Math.cos(angle+2.5) + ax, 0.2 * Math.sin(angle+2.5) + ay);
        lineDrawer.drawLine(screenConverter.realToScreen(rp), screenConverter.realToScreen(line.getP2()), Color.RED);
        lineDrawer.drawLine(screenConverter.realToScreen(rp2), screenConverter.realToScreen(line.getP2()), Color.RED);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) lastPosition = new ScreenPoint(e.getX(), e.getY());
        else if (e.getButton() == MouseEvent.BUTTON1) {
            currentNewLine = new Line(
                    screenConverter.screenToReal(new ScreenPoint(e.getX(), e.getY())),
                    screenConverter.screenToReal(new ScreenPoint(e.getX(), e.getY())),
                    Color.BLACK);
            MyLine = new Line(screenConverter.screenToReal(new ScreenPoint(e.getX(), e.getY())),
                    screenConverter.screenToReal(new ScreenPoint(e.getX(), e.getY())),
                    Color.RED);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) lastPosition = null;
        else if (e.getButton() == MouseEvent.BUTTON1) {
            allLines.add(MyLine);
            currentNewLine = null;
        }
        drawLine(lineDrawer, MyLine);
        System.out.println(allLines.size());
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        ScreenPoint currentPosition = new ScreenPoint(e.getX(), e.getY());
        cp = new ScreenPoint(e.getX(), e.getY());
        if (lastPosition != null) {
            ScreenPoint deltaScreen = new ScreenPoint(
                    currentPosition.getX() - lastPosition.getX(),
                    currentPosition.getY() - lastPosition.getY());
            RealPoint deltaReal = screenConverter.screenToReal(deltaScreen);
            RealPoint zeroReal = screenConverter.screenToReal(new ScreenPoint(0, 0));
            RealPoint vector = new RealPoint(
                    deltaReal.getX() - zeroReal.getX(),
                    deltaReal.getY() - zeroReal.getY());
            screenConverter.setCornerX(screenConverter.getCornerX() - vector.getX());
            screenConverter.setCornerY(screenConverter.getCornerY() - vector.getY());
            lastPosition = currentPosition;
        }
        if (currentNewLine != null) {
            currentNewLine.setP2(screenConverter.screenToReal(currentPosition));
        }
        RealPoint rp = new RealPoint(cp.getX()-50, cp.getY());
        RealPoint rp1 = new RealPoint(cp.getX(), cp.getY());
        RealPoint rp2 = new RealPoint(cp.getX()-150, cp.getY()-150);
        if (MyLine != null) {
            MyLine.setP2(screenConverter.screenToReal(currentPosition));
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        double scale = 1;
        double coef = clicks < 0 ? 1.1 : 0.9;
        for (int i = 0; i < Math.abs(clicks); i++) {
            scale *= coef;
        }
        screenConverter.setRealWidth(screenConverter.getRealWidth() * scale);
        screenConverter.setRealHeight(screenConverter.getRealHeight() * scale);
        repaint();
    }

    //}

    private void drawAxis(LineDrawer lineDrawer) {
        lineDrawer.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), Color.BLACK);
        lineDrawer.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, Color.BLACK);
    }

}


