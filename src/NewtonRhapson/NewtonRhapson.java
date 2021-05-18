package NewtonRhapson;

import Probe.ProbeSimulator;
import titan.State;
import titan.Vector;
import titan.Vector3dInterface;

import javax.swing.*;

public class NewtonRhapson {

    public Vector3dInterface findInitialVelocity(State initialState, Vector3dInterface finalPosition, double stepSize, double finalTime) {

        //TODO
        // calculate the trajectory

        //TODO
        // call getminimum and check if constraint is satisfied

        //TODO
        // calculate inverse jacobian matrix

            //TODO
            //calculate trajectories vx0 +h, vx0-h, vy0+h, ...
            //calculate derivatives

            //TODO
            //construct jacobian matrix
            //invert jacobian matrix

        //Find desired initial velocity
    }

    /**
     *  Will calculate the partial derivative of var1 to var 2
     * @param var1  first variable, will be slightly changed
     * @param var2
     * @param initialVelocity velocity to calculate the derivative
     * @return
     */
    public double getPartialDerivative(char var1, char var2, Vector3dInterface initialPosition, Vector3dInterface initialVelocity){
        double velocityComponent1 = getVelocityComponent(var1, initialVelocity);
        double velocityComponent2 = getVelocityComponent(var2, initialVelocity);

        double relativeChange = 0.0001;
        double change = Math.abs(velocityComponent1*relativeChange);

        ProbeSimulator probe = new ProbeSimulator();
        Vector3dInterface[] trajectoryPlus = probe.trajectoryAbsoluteInitialVelocity(initialPosition,initialVelocity, tf, h);
        Vector3dInterface[] trajectoryMinus =
        double valuePositiveChange =
        double valueNegativechange =
    }

    private double getVelocityComponent(char var, Vector3dInterface initialVelocity) {
        double velocityComponent = 0;
        switch (var) {
            case ('x') -> velocityComponent = initialVelocity.getX();
            case ('y') -> velocityComponent = initialVelocity.getY();
            case ('z') -> velocityComponent = initialVelocity.getZ();
        }
        return velocityComponent;
    }

    private Vector3dInterface changeVelocity(char var, double change, Vector3dInterface velocity){
        Vector3dInterface returnVelocity = new Vector();
        switch (var){
            case('x') -> returnVelocity = new Vector(velocity.getX() + change,velocity.getY(), velocity.getZ() );
            case('y') -> returnVelocity = new Vector(velocity.getX(), velocity.getY() + change, velocity.getZ() );
            case('z') -> returnVelocity = new Vector(velocity.getX(),velocity.getY(),velocity.getZ() + change );
        }
        return returnVelocity;
    }
}
