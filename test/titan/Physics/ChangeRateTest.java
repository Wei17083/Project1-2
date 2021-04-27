package titan.Physics;

import org.junit.jupiter.api.Test;
import titan.ChangeRate;
import titan.Vector;
import titan.Vector3dInterface;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import titan.*;

public class ChangeRateTest {
    @Test
    void getAdd(){
        Vector p1 = new Vector(2, 2, 2);
        Vector v1 = new Vector(4, 4, 4);
        ArrayList<Vector3dInterface> positionChanges = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityChanges = new ArrayList<>();
        positionChanges.add(p1);
        velocityChanges.add(v1);
        ChangeRate c = new ChangeRate();
        c.addPositionChange(p1);
        c.addVelocityChange(v1);
        assertTrue(VectorTools.equals(c.getPositionChanges().get(0), positionChanges.get(0)));
        assertTrue(VectorTools.equals(c.getVelocityChanges().get(0), velocityChanges.get(0)));
    }
}
