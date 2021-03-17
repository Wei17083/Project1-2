package titan;

import java.awt.event.KeyEvent;

public class GUI {
    private static final int msPerFrame = 100;
    private static double zoomOffsetX = 0;
    private static double zoomOffsetY = 0;

    public static void visualise(Body[] bodies) throws InterruptedException {
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
        // List<Vector3dInterface> earthPositions = new ArrayList<>();
        Vector3dInterface[] earthPositions = new Vector3dInterface[10000];
        // create list of position lists to iterate over
        // list<list<vector> positions = getPositions

        // draw initial positions
        StdDraw.clear(StdDraw.BLACK);
        for (Body body : bodies) {
            body.draw();
        }
        StdDraw.show();

        // TODO: Implement list of lists to draw all bodies
        for (Vector3dInterface pos : earthPositions) {
            StdDraw.clear(StdDraw.BLACK);
            for (Body body : bodies) {
                body.draw();
                // set body to new pos

            }
            StdDraw.show();

            // to focus on specific body set offsets like this
            // zoomOffsetX = titan.getPosition().getX();
            // zoomOffsetY = titan.getPosition().getY();

            // manual moving around
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                zoomOffsetX -= SolarSystem.getAU();
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                zoomOffsetX += SolarSystem.getAU();

            } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                zoomOffsetY -= SolarSystem.getAU();

            } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                zoomOffsetY += SolarSystem.getAU();
            }

            // zoom in (make scale smaller to zoom in)
            if (StdDraw.isKeyPressed(KeyEvent.VK_PLUS)) {
                scale /= 2;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_MINUS)) {
                scale *= 2;
            }
            StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
            StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());

            Thread.sleep(msPerFrame);
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
