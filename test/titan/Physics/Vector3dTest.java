package titan.Physics;

import static org.junit.jupiter.api.Assertions.*;
import titan.*;

import org.junit.jupiter.api.Test;

class Vector3dTest {

    @Test void testGetX() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(-1.1, v.getX());
    }

    @Test void testSetX() {
        Vector3dInterface v = new Vector();
        v.setX(-1.1);
        assertEquals(-1.1, v.getX());
    }

    @Test void testGetY() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(0.1, v.getY());
    }

    @Test void testSetY() {
        Vector3dInterface v = new Vector();
        v.setY(0.1);
        assertEquals(0.1, v.getY());
    }

    @Test void testGetZ() {
        Vector3dInterface v = new Vector(-1.1, 0.1, 1.1);
        assertEquals(1.1, v.getZ());
    }

    @Test void testSetZ() {
        Vector3dInterface v = new Vector();
        v.setZ(1.0);
        assertEquals(1.0, v.getZ());
    }

    @Test void testAddVector() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector( 0.5, 0.6, 0.7);
        Vector3dInterface ab = a.add(b);
        assertEquals(-1.1+0.5, ab.getX());
        assertEquals( 0.1+0.6, ab.getY());
        assertEquals( 1.1+0.7, ab.getZ());
    }

    @Test void testSub() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = new Vector( 0.5, 0.6, 0.7);
        Vector3dInterface ab = a.sub(b);
        assertEquals(-1.1-0.5, ab.getX());
        assertEquals( 0.1-0.6, ab.getY());
        assertEquals( 1.1-0.7, ab.getZ());
    }

    @Test void testMul() {
        Vector3dInterface a = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface b = a.mul(0.5);
        assertEquals(-1.1*0.5, b.getX());
        assertEquals( 0.1*0.5, b.getY());
        assertEquals( 1.1*0.5, b.getZ());
    }

    @Test void testAddMul() {
        Vector3dInterface a = new Vector( 0.6, 0.7, 0.8);
        Vector3dInterface b = new Vector(-1.1, 0.1, 1.1);
        Vector3dInterface ab = a.addMul(0.5, b);
        assertEquals(0.6 + 0.5*(-1.1), ab.getX());
        assertEquals(0.7 + 0.5*0.1,    ab.getY());
        assertEquals(0.8 + 0.5*1.1,    ab.getZ());
    }

    @Test void testNorm() {
        Vector3dInterface v = new Vector(3.0, -2.0, 6.0);
        assertEquals(7.0, v.norm());
    }

    @Test void testDist() {
        Vector3dInterface a = new Vector(3.0, 4.0,  8.0);
        Vector3dInterface b = new Vector(0.5, 0.25, 0.5);
        assertEquals(8.75, a.dist(b));
    }

    @Test void testToString() {
        Vector3dInterface v = new Vector(-1.1, 2.1, -3.1);
        String stringV = "(-1.1,2.1,-3.1)";
        assertEquals(stringV, v.toString());
    }

}
