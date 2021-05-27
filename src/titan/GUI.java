package titan;

import java.awt.event.KeyEvent;
import java.util.*;

import System.SolarSystem;

public class GUI {
    // has to be more than 100 000
    private static final int nsPerFrame = 1000000;
    private static double panOffsetX = 0;
    private static double panOffsetY = 0;
    private static double scale = 1;
    // animation only draws every nth position (n = skip)
    private static int skipSize = 50;
    private static int skip = skipSize;

    public static void visualise(Body[] bodies, List<List<Vector3dInterface>> allPositions,
                                 Vector3dInterface[] trajectory, int finalPos, int titanApproachPosition, List<Vector3dInterface> probeveloc) throws InterruptedException {
        StdDraw.enableDoubleBuffering(); // things are only drawn on next show()
        StdDraw.setCanvasSize(750, 750);

        // setting up scale in order to display whole solar system
        scale = 1;
        StdDraw.setXscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());
        StdDraw.setYscale(-scale * SolarSystem.getAU(), scale * SolarSystem.getAU());

        // set up offset to start on earth
        panOffsetX = bodies[3].getPosition().getX();
        panOffsetY = bodies[3].getPosition().getY();

        String animationSpeed = "Speed: " + skipSize + "x";
        String probeVelocity = "Velocity: " + skipSize + " m/s";
        String instructions = "Start/Stop: Spacebar, Follow Probe: F, Zoom: +/-, Pan: WASD or arrows, Restart: R";

        // animation phases
        int phase2 = (int) (titanApproachPosition * 0.9);
        int phase3 = (int) (titanApproachPosition * 1.015);
        int phase4 = (int) (finalPos * 0.9);

        // indicates whether the automatic panning is active or not
        boolean interrupted = false;
        // indicates whether the automatic zoom is active or not
        boolean zoomInterrupted = false;
        // indicates whether the animation is paused or not
        boolean paused = true;
        // lock to make sure one button press doesnt toggle pause on/off multiple times
        boolean pauseLock = false;

        // convert nsPerFrame to a ms double
        double msPerFrame = nsPerFrame / 1000000;

        // draw initial positions
        StdDraw.clear(StdDraw.BLACK);
        for (Body body : bodies) {
            body.draw();
        }
        VectorTools.drawProbe(trajectory[0], probeveloc.get(0));
        StdDraw.show();

        // start animation loop (animation starts paused)
        for (int i = 0; i < allPositions.get(0).size(); i += skip) {
            //slow down around titan
            if (i > titanApproachPosition * 0.99 - skipSize && i < titanApproachPosition * 1.02) {
                skip = 1;
            } else if (i >= finalPos - 3 * skipSize) {
                skip = 1; // slow down for last frames
                if (i > finalPos - 1)
                    i--; // stay on last position
            } else skip = skipSize;
            animationSpeed = "Speed: " + skip + "x";
            probeVelocity = "Velocity: " + (int) probeveloc.get(i).norm() + " m/s";
            ;

            // pause/unpause entire animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                if (!pauseLock) {
                    paused = !paused; // toggle pause
                    pauseLock = true; // lock pause button
                }
            }
            if (!StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
                pauseLock = false; // unlock pause once button released

            // restart animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                // reset all values
                paused = true;
                interrupted = false;
                zoomInterrupted = false;
                i = 0;
                for (int j = 0; j < bodies.length; j++) { // redraw bodies
                    bodies[j].setPosition(allPositions.get(j).get(i));
                }
                skip = skipSize;
                scale = 1;
                panOffsetX = trajectory[i].getX();
                panOffsetY = trajectory[i].getY();
            }

            // manual moving around
            // press space to continue the auto animation
            if (StdDraw.isKeyPressed(KeyEvent.VK_F)) {
                interrupted = false;
                zoomInterrupted = false;
                // set pan to follow probe
                panOffsetX = trajectory[i].getX();
                panOffsetY = trajectory[i].getY();
            }

            // changes are scaling based on log of msPerFrame*10 so that the zoom effect.
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
                        if (i <= phase2 || (i >= phase3 && i < phase4)) {
                            if (scale < 1 &&(int) probeveloc.get(i).norm()>20000)
                                scale += 0.005; //reset zoom when speeding away from titan
                            else if (scale < 4)
                                scale *= 1.0 + (Math.log(msPerFrame * 10) / 150);
                            else if (scale < 8) { // quickly zoom out
                                scale *= 1.0 + (Math.log(msPerFrame * 10) / 250);
                            } else if (scale > 8.1) {
                                scale /= 1.0 + (Math.log(msPerFrame * 10) / 1000);
                            }
                        } else if ((i > phase2 && i < phase3) || i >= phase4) {
                            if (scale > 0.0001) { // zoom in
                                scale /= 1.0 + (Math.log(msPerFrame * 10) / 200);
                            } else if (scale < 0.00005) {
                                scale *= 1.0 + (Math.log(msPerFrame * 10) / 10000);
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
                VectorTools.drawProbe(trajectory[0], probeveloc.get(0));
            else if (i > finalPos) {
                VectorTools.drawProbe(trajectory[finalPos], probeveloc.get(probeveloc.size() - 1));
            } else {
                VectorTools.drawProbe(trajectory[i], probeveloc.get(i));
            }
            StdDraw.text(-0.8 * scale * SolarSystem.AU, +0.9 * scale * SolarSystem.AU, animationSpeed);
            StdDraw.text(+0.7 * scale * SolarSystem.AU, +0.9 * scale * SolarSystem.AU, probeVelocity);
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
