package titan.Physics;
import org.junit.jupiter.api.Test;
import titan.Body;
import titan.Vector;
import titan.VectorTools;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class BodyTest {
    @Test
    void getSet() {
        Vector p = new Vector(1, 1, 1);
        Vector v = new Vector(2, 2, 2);
        Body b = new Body("test", 10, 100, p, v, 100, null);
        assertEquals(b.getName(), "test");
        assertEquals(b.getID(), 10);
        assertEquals(b.getMass(), 100);
        assertTrue(VectorTools.equals(b.getPosition(), p));
        assertTrue(VectorTools.equals(b.getVelocity(), v));
        assertEquals(b.getRadius(), 100);
        assertEquals(b.getColor(), null);

        b.setName("test 1");
        b.setMass(200);
        b.setPosition(new Vector(3,3,3));
        b.setVelocity(new Vector (4,4,4));
        b.setColor(new Color(1, 1, 1));
        assertEquals(b.getName(), "test 1");
        assertEquals(b.getMass(), 200);
        assertTrue(VectorTools.equals(b.getPosition(), new Vector(3,3,3)));
        assertTrue(VectorTools.equals(b.getVelocity(), new Vector(4,4,4)));
        assertEquals(b.getColor(), new Color(1, 1, 1));
    }



}
