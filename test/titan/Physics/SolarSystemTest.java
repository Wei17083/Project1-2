package titan.Physics;

import org.junit.jupiter.api.Test;
import titan.*;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SolarSystemTest {
    @Test
    void getSet() {
        Vector v1 = new Vector(1,1,1);
        Vector p1 = new Vector(1,1,1);
        Vector v2 = new Vector(2,2,2);
        Vector p2 = new Vector(2,2,2);
        Body b1 = new Body("test1", 1, 100, p1, v1, 100, new Color(1,1,1));
        Body b2 = new Body("test2", 2, 200, p2, v2, 200, new Color(2,2,2));
        Body[] bodies = {b1, b2};
        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        for (Body b : bodies) {
            positionList.add(b.getPosition());
            velocityList.add(b.getVelocity());
        }
        State state = new State(0, positionList, velocityList);
        SolarSystem system = new SolarSystem(bodies);
        assertEquals(state, system.getState());
        assertEquals(bodies, system.getBodies());
    }

    @Test
    void gravitationalPull() {
        Vector p1 = new Vector(0, 0, 0);
        Vector v = new Vector(0, 0, 0);
        Body b1 = new Body("body1", 10, 100, p1, v, 0, null);

        Vector p2 = new Vector(10, 0, 0);
        Body b2 = new Body("body2", 10, 100, p2, v, 0, null);
        Body[] bodies = {b1, b2};
        SolarSystem system = new SolarSystem(bodies);

        double gravConstant = 6.674E-11;
        double gravitationalForce = gravConstant * b1.getMass() * b2.getMass() / Math.pow(b1.getPosition().dist(b2.getPosition()), 2);
        Vector3dInterface direction = VectorTools.directionVector(p2, p1);
        Vector3dInterface forceVector = direction.mul(gravitationalForce);
        System.out.println(VectorTools.vectorToString(system.gravitationalPull(b1.getMass(), b2.getMass(), b1.getPosition(), b2.getPosition())));
        System.out.println(VectorTools.vectorToString(forceVector));
        assertTrue(VectorTools.equals(forceVector, system.gravitationalPull(b1.getMass(), b2.getMass(), b1.getPosition(), b2.getPosition())));
    }
}
