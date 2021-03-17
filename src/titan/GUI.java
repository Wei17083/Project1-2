package titan;

import java.awt.event.KeyEvent;
import java.util.*;

public class GUI {
    // has to be more than 100 000
    private static final int nsPerFrame = 1000000;
    private static double zoomOffsetX = 0;
    private static double zoomOffsetY = 0;
    // animation only draws every nth position (n = skip)
    private static int skip = 10;

    public static void visualise(Body[] bodies, List<Vector3dInterface> earthPositions) throws InterruptedException {
        StdDraw.enableDoubleBuffering(); // things are only drawn on next show()
        StdDraw.setCanvasSize(750, 750);
        // setting up scale in order to display whole solar system
        double scale = 14;
        StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());

        // TODO: Calculate all planet positions in advance and store them.
        // that way we can skip most of the following loop, and only calculate the
        // forces acting on the probe

        // get all body position lists
        // example values to test animation
        // Vector3dInterface[] earthPositions = new Vector3dInterface[10000];
        // create list of position lists to iterate over
        // list<list<vector> positions = getPositions

        // draw initial positions
        StdDraw.clear(StdDraw.BLACK);
        for (Body body : bodies) {
            body.draw();
        }
        StdDraw.show();

        // convert nsPerFrame to a ms double
        double msPerFrame = nsPerFrame / 1000000;

        // TODO: Implement list of lists to draw all bodies
        for (int i = 0; i < earthPositions.size(); i += skip) {

            StdDraw.clear(StdDraw.BLACK);
            bodies[3].setPosition(earthPositions.get(i));
            for (Body body : bodies) {
                // set body to new pos
                // body.setPosition(pos or whatever)
                body.draw();
            }
            StdDraw.show();

            // to focus on specific body set offsets like this
            // zoomOffsetX = titan.getPosition().getX();
            // zoomOffsetY = titan.getPosition().getY();

            // manual moving around

            // changes are scaling based on log of msPerFrame*10 so that the zoom effect
            // stays similar even when framerate changes.
            // msPerFrame has to be more than 0.1 for this to work, else log(1) = 0
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                zoomOffsetX -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_D)) {
                zoomOffsetX += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;

            } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_S)) {
                zoomOffsetY -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;

            } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_W)) {
                zoomOffsetY += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            }

            // zoom (make scale smaller to zoom in)
            if (StdDraw.isKeyPressed(KeyEvent.VK_PLUS) || StdDraw.isKeyPressed(107)) {
                scale /= 1.0 + (Math.log(msPerFrame * 10) / 40);
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_MINUS) || StdDraw.isKeyPressed(109)) {
                scale *= 1.0 + (Math.log(msPerFrame * 10) / 40);
            }
            StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
            StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());

            // convert to int because sleep() doesnt take double
            int ms = nsPerFrame / 1000000;
            int ns = nsPerFrame % 1000000;
            Thread.sleep(ms, ns);
        }

    }

    public static double getZoomOffsetX() {
        return zoomOffsetX;
    }

    public static void setZoomOffsetX(double zoomOffsetX) {
        GUI.zoomOffsetX = zoomOffsetX;
    }

    public static double getZoomOffsetY() {
        return zoomOffsetY;
    }

    public static void setZoomOffsetY(double zoomOffsetY) {
        GUI.zoomOffsetY = zoomOffsetY;
    }

}
