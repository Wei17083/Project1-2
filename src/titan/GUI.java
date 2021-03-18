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

    public static void visualise(Body[] bodies, List<List<Vector3dInterface>> allPositions,
            Vector3dInterface[] trajectory) throws InterruptedException {
        StdDraw.enableDoubleBuffering(); // things are only drawn on next show()
        StdDraw.setCanvasSize(750, 750);

        // setting up scale in order to display whole solar system
        scale = 1;
        StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        // set up offset to start on earth
        panOffsetX = bodies[3].getPosition().getX();
        panOffsetY = bodies[3].getPosition().getY();

        String instructions = "Start: O, Stop: P, Follow Probe: Spacebar, Zoom: +/-, Pan: WASD or arrows";
        Body probe = bodies[11];

        // phase 2
        int phase2 = allPositions.get(0).size() / 4 * 3;

        // indicates wether the automatic panning is active or not
        boolean interrupted = false;
        // indicates wether the automatic zoom is active or not
        boolean zoomInterrupted = false;
        // indicates wether the animation is paused or not
        boolean paused = true;

        // convert nsPerFrame to a ms double
        double msPerFrame = nsPerFrame / 1000000;

        // draw initial positions
        StdDraw.clear(StdDraw.BLACK);
        for (Body body : bodies) {
            body.draw();
        }
        VectorTools.drawProbe(trajectory[0]);
        StdDraw.show();

        // start animation loop (animation starts paused)
        for (int i = 0; i < allPositions.get(0).size(); i += skip) {
            // pause entire animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_P))
                paused = true;
            // unpause entire animation
            else if (StdDraw.isKeyPressed(KeyEvent.VK_O))
                paused = false;

            // manual moving around
            // press space to continue the auto animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                interrupted = false;
                zoomInterrupted = false;
                // set pan to follow probe
                panOffsetX = trajectory[i].getX();
                panOffsetY = trajectory[i].getY();
            }

            // changes are scaling based on log of msPerFrame*10 so that the zoom effect
            // stays similar even when framerate changes.
            // msPerFrame has to be more than 0.1 for this to work, else log(1) = 0
            else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                interrupted = true;
                panOffsetX -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_D)) {
                interrupted = true;
                panOffsetX += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_S)) {
                interrupted = true;
                panOffsetY -= SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_W)) {
                interrupted = true;
                panOffsetY += SolarSystem.getAU() * Math.log(msPerFrame * 10) / 100 * scale;
            }
            // zoom (make scale smaller to zoom in)
            if (StdDraw.isKeyPressed(KeyEvent.VK_PLUS) || StdDraw.isKeyPressed(107)) {
                scale /= 1.0 + (Math.log(msPerFrame * 10) / 40);
                zoomInterrupted = true;
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_MINUS) || StdDraw.isKeyPressed(109)) {
                scale *= 1.0 + (Math.log(msPerFrame * 10) / 40);
                zoomInterrupted = true;
            }

            if (!paused) {

                // set bodies to new pos
                for (int j = 0; j < bodies.length; j++) {
                    bodies[j].setPosition(allPositions.get(j).get(i));
                }

                // auto animation following probe
                if (!interrupted) {
                    // set pan to follow probe
                    panOffsetX = trajectory[i].getX();
                    panOffsetY = trajectory[i].getY();
                    if (!zoomInterrupted) {
                        if (i <= phase2) {
                            if (scale < 8) { // slowly zoom out
                                // scale = Math.pow(1.0 + (Math.log(msPerFrame * 10) / 10000), i);
                                // phase1EndScale = scale;
                                scale *= 1.0 + (Math.log(msPerFrame * 10) / 1000);
                            } else if (scale > 8.1) {
                                scale /= 1.0 + (Math.log(msPerFrame * 10) / 1000);
                            }
                        } else if (i > phase2) {
                            if (scale > 1) { // slowly zoom in
                                // scale = phase1EndScale + 1.0
                                // - Math.pow(1.0 + (Math.log(msPerFrame * 10) / 10000), (i - phase2));
                                scale /= 1.0 + (Math.log(msPerFrame * 10) / 1000);
                            } else if (scale < 0.9) {
                                scale *= 1.0 + (Math.log(msPerFrame * 10) / 1000);
                            }
                        }
                    }
                }

            } else {
                // if animation is paused, dont progress i
                i -= skip;
            }
            // clear canvas
            StdDraw.clear(StdDraw.BLACK);
            // draw new bodies
            for (Body body : bodies) {
                body.draw();
            }
            if (i <= 0) // if animation hasnt started yet
                VectorTools.drawProbe(trajectory[0]);
            else {
                VectorTools.drawProbe(trajectory[i]);
            }
            StdDraw.text(0, -0.9 * scale * SolarSystem.AU, instructions);
            StdDraw.show();
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
