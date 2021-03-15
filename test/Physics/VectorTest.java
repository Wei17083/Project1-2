package Physics;

import titan.Vector3dInterface;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @org.junit.jupiter.api.Test
    void getSet() {
        Vector v = new Vector(1,2,3);
        assertEquals(v.getX(), 1);
        assertEquals(v.getY(), 2);
        assertEquals(v.getZ(), 3);

        v.setX(2);
        v.setY(3);
        v.setZ(4);
        assertEquals(v.getX(), 2);
        assertEquals(v.getY(), 3);
        assertEquals(v.getZ(), 4);
    }


    @org.junit.jupiter.api.Test
    void add() {
        Vector v1 = new Vector(1,2,3);
        Vector v2 = new Vector(1,1,1);
        Vector3dInterface v3 = v1.add(v2);
        assertEquals(v3.getX(), 2);
        assertEquals(v3.getY(), 3);
        assertEquals(v3.getZ(), 4);
    }

    @org.junit.jupiter.api.Test
    void sub() {
        Vector v1 = new Vector(1,2,3);
        Vector v2 = new Vector(1,1,1);
        Vector3dInterface v3 = v1.sub(v2);
        assertEquals(v3.getX(), 0);
        assertEquals(v3.getY(), 1);
        assertEquals(v3.getZ(), 2);
    }

    @org.junit.jupiter.api.Test
    void mul() {
        Vector v1 = new Vector(1,2,3);
        Vector3dInterface v2 = v1.mul(2);
        assertEquals(2, v2.getX());
        assertEquals(4, v2.getY());
        assertEquals(6, v2.getZ());
    }

    @org.junit.jupiter.api.Test
    void addMul() {
        Vector v1 = new Vector(1,2,3);
        Vector v2 = new Vector(1,1,1);
        Vector3dInterface v3 = v1.addMul(2, v2);
        assertEquals(3,v3.getX());
        assertEquals(4,v3.getY());
        assertEquals(5,v3.getZ());
    }

    @org.junit.jupiter.api.Test
    void norm() {
        Vector v1 = new Vector(3,4,0);
        assertEquals(5, v1.norm());
    }

    @org.junit.jupiter.api.Test
    void dist() {
        Vector v1 = new Vector(0,0,0);
        Vector v2 = new Vector(3,4,0);
        assertEquals(5, v1.dist(v2));
    }
}