package titan.Physics;

import org.junit.jupiter.api.Test;
import titan.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateTest {
    @Test
    void get() {
        Vector p = new Vector(1, 1, 1);
        Vector v = new Vector(2, 2, 2);
        Vector p1 = new Vector(3, 3, 3);
        Vector v1 = new Vector(4, 4, 4);
        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        positionList.add(p);
        positionList.add(p1);
        velocityList.add(v);
        velocityList.add(v1);
        State s = new State(0, positionList, velocityList);
        assertTrue(VectorTools.equals(s.getPositionList().get(0), p));
        assertTrue(VectorTools.equals(s.getVelocityList().get(0), v));
        assertEquals(s.getStateTime(), 0);
    }
    @Test
    void addMul(){
        Vector p = new Vector(1, 1, 1);
        Vector v = new Vector(2, 2, 2);
        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        positionList.add(p);
        velocityList.add(v);
        State s = new State(0, positionList, velocityList);
        Vector p1 = new Vector(2, 2, 2);
        Vector v1 = new Vector(4, 4, 4);
        ArrayList<Vector3dInterface> positionChanges = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityChanges = new ArrayList<>();
        positionChanges.add(p1);
        velocityChanges.add(v1);
        ChangeRate c = new ChangeRate();
        c.addPositionChange(p1);
        c.addVelocityChange(v1);
        Vector pNew = new Vector(0.3, 0.3, 0.3);
        Vector vNew = new Vector(0.6, 0.6, 0.6);
        ArrayList<Vector3dInterface> positionListNew = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityListNew = new ArrayList<>();
        positionListNew.add(pNew);
        velocityListNew.add(vNew);
        State sNew = (State) s.addMul(0.1, c);
        assertTrue(VectorTools.equals(positionListNew.get(0), sNew.getPositionList().get(0)));
        assertTrue(VectorTools.equals(velocityListNew.get(0), sNew.getVelocityList().get(0)));
        assertEquals(0, sNew.getStateTime());
    }

    @Test
    void string(){

    }
}
