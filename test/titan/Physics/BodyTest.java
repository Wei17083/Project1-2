//package titan.Physics;
//
//import org.junit.jupiter.api.Test;
//
//import titan.Body;
//import titan.Vector;
//import titan.Vector3dInterface;
//import titan.VectorTools;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BodyTest {
//
//    @Test
//    void getSet() {
//        Vector p = new Vector(1, 1, 1);
//        Vector v = new Vector(2, 2, 2);
//        Body b = new Body("test", 10, p, v, 100, null);
//
//    }
//
//    @Test
//    void gravitationalPull() {
//        Vector p1 = new Vector(0,0,0);
//        Vector v = new Vector(0,0,0);
//        Body b1 = new Body("body1",10, p1, v, 0, null);
//
//        Vector p2 = new Vector(10, 0, 0);
//        Body b2 = new Body("body2",10, p2, v, 0, null);
//        double gravConstant = 6.674E-11;
//        double gravitationalForce = gravConstant*b1.getMass()*b2.getMass()/Math.pow(b1.getPosition().dist(b2.getPosition()), 2);
//        Vector3dInterface direction = VectorTools.directionVector(p2, p1);
//        Vector3dInterface forceVector = direction.mul(gravitationalForce);
//        System.out.println(VectorTools.vectorToString(b1.gravitationalPull(b2)));
//        System.out.println(VectorTools.vectorToString(forceVector));
//        assertTrue(VectorTools.equals(forceVector, b1.gravitationalPull(b2)));
//    }
//}