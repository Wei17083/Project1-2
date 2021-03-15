package Physics;

import java.awt.*;

import titan.FunctionInterface;
import titan.Vector3dInterface;

public class SolarSystem implements FunctionInterface {

    public final double GRAV_CONSTANT = 5;
    // One AU is approximately the average distance between the Earth and the Sun
    // value taken from https://cneos.jpl.nasa.gov/glossary/au.html
    public static final double AU = 1.495978707e11;
    private static int maxSteps = 1000;

    public static void main(String[] args) {
        // radius taken from
        // https://solarsystem.nasa.gov/solar-system
        // other values taken from solar_system_data-2020_04_01.txt
        Body sun = new Body("Sun", 1.988500e30,
                new Vector(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06),
                new Vector(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01), 6.95508e8,
                Color.orange);

        Body earth = new Body("Earth", 5.97219e24,
                new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06),
                new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01), 6.371e6, Color.green);

        Body saturn = new Body("Saturn", 5.6834e26,
                new Vector(6.328646641500651e+11, -1.358172804527507e+12, -1.578520137930810e+09),
                new Vector(8.220842186554890e+03, 4.052137378979608e+03, -3.976224719266916e+02), 5.8232e7,
                Color.yellow);

        Body titan = new Body("Titan", 1.34553e23,
                new Vector(6.332873118527889e+11, -1.357175556995868e+12, -2.134637041453660e+09),
                new Vector(3.056877965721629e+03, 6.125612956428791e+03, -9.523587380845593e+02), 2.5755e6,
                Color.yellow);

        // create arrays of bodies and corresponding forces
        Body[] bodies = new Body[] { sun, earth, saturn, titan }; // sort bodies by distance from sun {sun, planet,
                                                                  // moons, next planet}
        Vector[] forceOnBody = new Vector[bodies.length];

        // setting up scale for easier calculations

        StdDraw.enableDoubleBuffering(); // things are only drawn on next show()
        StdDraw.setCanvasSize(750, 750);
        int scale = 10;
        StdDraw.setXscale(-scale * AU, scale * AU);
        StdDraw.setYscale(-scale * AU, scale * AU);
        // TODO: Calculate all planet positions and store them

        for (int i = 0; i < maxSteps; i++) {
            StdDraw.clear(Color.black);
            for (int j = 0; j < bodies.length; j++) {
                bodies[j].draw();
                forceOnBody[j] = new Vector(0, 0, 0); // reset force
                for (Body body : bodies) {
                    forceOnBody[j].add(body.getGravitationalForce(bodies[j]));
                }
                // body.getVelocity().add(gravitational force);
                bodies[j].getPosition().add(bodies[j].getVelocity());
            }
            StdDraw.show(); // show the current system
        }

    }

    @Override
    public Vector3dInterface call(double t, Vector3dInterface s) {
        return null;
    }
}
