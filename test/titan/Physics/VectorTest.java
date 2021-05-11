package titan.Physics;

import titan.Vector;
import titan.Vector3dInterface;
import titan.VectorTools;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @org.junit.jupiter.api.Test
    void testGetX() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(-1.1, v.getX());
    }

    @org.junit.jupiter.api.Test
    void testSetX() {
        Vector3dInterface v = new Vector();
        v.setX(-1.1);
        assertEquals(-1.1, v.getX());
    }

    @org.junit.jupiter.api.Test
    void testGetY() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(0.1, v.getY());
    }

    @org.junit.jupiter.api.Test
    void testSetY() {
        Vector3dInterface v = new Vector();
        v.setY(0.1);
        assertEquals(0.1, v.getY());
    }

    @org.junit.jupiter.api.Test
    void testGetZ() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(1.1, v.getZ());
    }

    @org.junit.jupiter.api.Test
    void testSetZ() {
        Vector3dInterface v = new Vector();
        v.setZ(1.0);
        assertEquals(1.0, v.getZ());
    }

    @org.junit.jupiter.api.Test
    void testAddVector3d() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector( 0.5, 0.6, 0.7);
        Vector3dInterface ab = a.add(b);
        assertEquals(-1.1+0.5, ab.getX());
        assertEquals( 0.1+0.6, ab.getY());
        assertEquals( 1.1+0.7, ab.getZ());
    }

    @org.junit.jupiter.api.Test
    void testSub() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector( 0.5, 0.6, 0.7);
        Vector3dInterface ab = a.sub(b);
        assertEquals(-1.1-0.5, ab.getX());
        assertEquals( 0.1-0.6, ab.getY());
        assertEquals( 1.1-0.7, ab.getZ());
    }

    @org.junit.jupiter.api.Test
    void testMul() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = a.mul(0.5);
        assertEquals(-1.1*0.5, b.getX());
        assertEquals( 0.1*0.5, b.getY());
        assertEquals( 1.1*0.5, b.getZ());
    }

    @org.junit.jupiter.api.Test
    void testAddMul() {
        Vector3dInterface a = new Vector( 0.6, 0.7, 0.8);
        Vector3dInterface b = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface ab = a.addMul(0.5, b);
        assertEquals(0.6 + 0.5*(-1.1), ab.getX());
        assertEquals(0.7 + 0.5*0.1,    ab.getY());
        assertEquals(0.8 + 0.5*1.1,    ab.getZ());
    }

    @org.junit.jupiter.api.Test
    void testNorm() {
        Vector3dInterface v = new Vector(3.0, -2.0, 6.0);
        assertEquals(7.0, v.norm());
    }

    @org.junit.jupiter.api.Test
    void testDist() {
        Vector3dInterface a = new Vector(3.0, 4.0,  8.0);
        Vector3dInterface b = new Vector(0.5, 0.25, 0.5);
        assertEquals(8.75, a.dist(b));
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Vector3dInterface v = new Vector(-1.1, 2.1, -3.1);
        String stringV = "(-1.1,2.1,-3.1)";
        assertEquals(stringV, v.toString());
    }


    @org.junit.jupiter.api.Test
    void directionUnitVector() {
        Vector v1 = new Vector(1,0,1);
        Vector v2 = new Vector(1,9,1);
        Vector3dInterface v3 = VectorTools.directionVector(v1, v2);
        assertEquals(1, v3.getY());

    }

    @org.junit.jupiter.api.Test
    void sumAll() {
        Vector v1 = new Vector(1,0,1);
        Vector v2 = new Vector(1,9,1);
        Vector v3 = new Vector(3,9,-8);
        ArrayList<Vector3dInterface> vectorList = new ArrayList<>();
        vectorList.add(v1);
        vectorList.add(v2);
        vectorList.add(v3);
        Vector3dInterface v4 = VectorTools.sumAll(vectorList);
        assertTrue(VectorTools.equals(v4, new Vector(5, 18, -6)));
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Vector3dInterface x = new Vector(2,5,7);
        Vector3dInterface y = new Vector(2,5,7);
        assertTrue(VectorTools.equals(x,y));
    }
}