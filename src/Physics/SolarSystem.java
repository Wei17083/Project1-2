package Physics;

import java.awt.*;

import titan.FunctionInterface;
import titan.Vector3dInterface;

public class SolarSystem implements FunctionInterface {

    public final double GRAV_CONSTANT = 5;

    public static void main(String[] args) {
        // radius taken from
        // https://solarsystem.nasa.gov/solar-system
        // other values taken from solar_system_data-2020_04_01.txt
        Body sun = new Body("Sol", 1.988500e30,
                new Vector(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06),
                new Vector(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01), 4.3700056e9,
                Color.orange);

        Body earth = new Body("Earth", 5.97219e24,
                new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06),
                new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01), 6371e3, Color.orange);
    }

    @Override
    public Vector3dInterface call(double t, Vector3dInterface s) {
        return null;
    }
}
