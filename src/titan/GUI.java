package titan;

import java.awt.event.KeyEvent;
import java.util.*;

public class GUI {
    // has to be more than 100 000
    private static final int nsPerFrame = 1000000;
    private static double panOffsetX = 0;
    private static double panOffsetY = 0;
    private static double scale = 1;
    // animation only draws every nth position (n = skip)
    private static int skip = 10;

    public static void visualise(Body[] bodies, List<List<Vector3dInterface>> allPositions)
            throws InterruptedException {
        StdDraw.enableDoubleBuffering(); // things are only drawn on next show()
        StdDraw.setCanvasSize(750, 750);
        // setting up scale in order to display whole solar system
        scale = 1;
        StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        // set up offset to start on earth
        // TODO: double check that index 3 is always earth
        panOffsetX = bodies[3].getPosition().getX();
        panOffsetY = bodies[3].getPosition().getY();

        Vector3dInterface titanEndPos = allPositions.get(8).get(allPositions.size() - 1);
        Vector3dInterface earthStartPos = allPositions.get(3).get(0);
        // phase 1
        int phase1 = allPositions.get(0).size() / 2;
        double phase1EndScale = scale;

        // phase 2
        int phase2 = allPositions.get(0).size() / 4 * 3;

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
        for (int i = 0; i < allPositions.get(0).size(); i += skip) {
            StdDraw.clear(StdDraw.BLACK);
            // set bodies to new pos
            for (int j = 0; j < bodies.length; j++) {
                bodies[j].setPosition(allPositions.get(j).get(i));
            }
            // draw bodies
            for (Body body : bodies) {
                body.draw();
            }
            StdDraw.show();

            // manual moving around
            // press space to continue the auto animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
                interrupted = false;

            // changes are scaling based on log of msPerFrame*10 so that the zoom effect
            // stays similar even when framerate changes.
            // msPerFrame has to be more than 0.1 for this to work, else log(1) = 0
            else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                interrupted = true;
                panOffsetX -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_D)) {
                interrupted = true;
                panOffsetX += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_S)) {
                interrupted = true;
                panOffsetY -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_W)) {
                interrupted = true;
                panOffsetY += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            }
            // zoom (make scale smaller to zoom in)
            if (StdDraw.isKeyPressed(KeyEvent.VK_PLUS) || StdDraw.isKeyPressed(107)) {
                scale /= 1.0 + (Math.log(msPerFrame * 10) / 40);
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_MINUS) || StdDraw.isKeyPressed(109)) {
                scale *= 1.0 + (Math.log(msPerFrame * 10) / 40);
            }

            // auto animation
            if (!interrupted) {
                if (i <= phase1) {
                    if (scale < 8) { // slowly zoom out
                        scale = Math.pow(1.0 + (Math.log(msPerFrame * 10) / 10000), i);
                        phase1EndScale = scale;
                    }
                } else if (i > phase2) {
                    if (scale > 1) // slowly zoom in
                        scale = phase1EndScale + 1.0
                                - Math.pow(1.0 + (Math.log(msPerFrame * 10) / 10000), (i - phase2));
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
        return panOffsetX;
    }

    public static double getScale() {
        return scale;
    }

    public static void setZoomOffsetX(double zoomOffsetX) {
        GUI.panOffsetX = zoomOffsetX;
    }

    public static double getZoomOffsetY() {
        return panOffsetY;
    }

    public static void setZoomOffsetY(double zoomOffsetY) {
        GUI.panOffsetY = zoomOffsetY;
    }

}
