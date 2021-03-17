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
        double scale = 1;
        StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        // set up offset to start on earth
        // TODO: double check that index 3 is always earth
        zoomOffsetX = bodies[3].getPosition().getX();
        zoomOffsetY = bodies[3].getPosition().getY();

        // time stamps for automatic zoom
        int zoomOut = earthPositions.size() / 2;
        int stay = earthPositions.size() / 4 * 3;
        // indicates wether the automatic zoom is active or not
        boolean interrupted = false;
        // indicates wether the animation is paused or not
        boolean paused = false;

        // convert nsPerFrame to a ms double
        double msPerFrame = nsPerFrame / 1000000;

        // TODO: Calculate all planet positions in advance and store them.

        // draw initial positions
        StdDraw.clear(StdDraw.BLACK);
        for (Body body : bodies) {
            body.draw();
        }
        StdDraw.show();

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
            // if we press a button, interrupt the auto animation
            if (StdDraw.isKeyPressed(KeyEvent.KEY_PRESSED) && !StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
                interrupted = true;

            // changes are scaling based on log of msPerFrame*10 so that the zoom effect
            // stays similar even when framerate changes.
            // msPerFrame has to be more than 0.1 for this to work, else log(1) = 0
            if (interrupted) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_A))
                    zoomOffsetX -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
                else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_D))
                    zoomOffsetX += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
                else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_S))
                    zoomOffsetY -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
                else if (StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_W))
                    zoomOffsetY += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;

                // zoom (make scale smaller to zoom in)
                if (StdDraw.isKeyPressed(KeyEvent.VK_PLUS) || StdDraw.isKeyPressed(107)) {
                    scale /= 1.0 + (Math.log(msPerFrame * 10) / 40);
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_MINUS) || StdDraw.isKeyPressed(109)) {
                    scale *= 1.0 + (Math.log(msPerFrame * 10) / 40);
                }
            } else {
                if (i <= zoomOut) {
                    if (scale < 8) // slowly zoom out
                        scale = Math.pow(1.0 + (Math.log(msPerFrame * 10) / 4000), i);
                    else {
                        if (zoomOffsetX < (bodies[4].getPosition().getX() + bodies[8].getPosition().getX()) / 2)
                            zoomOffsetX += (bodies[4].getPosition().getX() + bodies[8].getPosition().getX()) / 1000;
                        if (zoomOffsetY > (bodies[4].getPosition().getY() + bodies[8].getPosition().getY()) / 2)
                            zoomOffsetY += (bodies[4].getPosition().getY() + bodies[8].getPosition().getY()) / 1000;
                    }

                }
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
