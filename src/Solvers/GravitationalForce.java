package Solvers;

import titan.Vector3dInterface;
import titan.VectorTools;

public class GravitationalForce {

    /** calculates gravitational force from mass2 on mass 1 (direction from p2 to p1)
     *
     * @param p1    position of the body to calculate force on
     * @param p2    position that exerts the force on body 1
     * @param mass1 mass of the body the force is exerted on
     * @param mass2 mass of the body that exerts the force
     * @return forceVector that mass 2 exerts on mass 1
     */
    public static Vector3dInterface gravitationalForce(Vector3dInterface p1, Vector3dInterface p2, double mass1, double mass2) {
        return acceleration(p1, p2, mass2).mul(mass1);
    }

    /** Calculates the acceleration at point p due to the presence of a mass at point pMass
     *
     * @param p point at which to evaluate the acceleration
     * @param pMass position of the Mass
     * @param mass mass of the body resposnsoble for the acceleration at point p
     * @return acceleration at point p
     */
    public static Vector3dInterface acceleration(Vector3dInterface p, Vector3dInterface pMass, double mass) {
        final double GRAV_CONSTANT = 6.674E-11;
        double distance = pMass.dist(p);
        Vector3dInterface accelerationDirection = VectorTools.directionVector(p, pMass);
        double force = GRAV_CONSTANT * mass / Math.pow(distance, 2);
        return accelerationDirection.mul(force);
    }
}
