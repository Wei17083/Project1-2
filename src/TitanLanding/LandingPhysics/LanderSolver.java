package TitanLanding.LandingPhysics;

import TitanLanding.Lander.Lander;
import TitanLanding.Lander.LanderState;
import titan.Vector3dInterface;

public class LanderSolver {

    private Vector3dInterface totalForce;
    private double torque;
    private final Lander lander;

    public LanderSolver(Vector3dInterface totalForce, double torque, Lander lander) {
        this.totalForce = totalForce;
        this.torque = torque;
        this.lander = lander;
    }

    public LanderState stateStep(LanderState state, double stepSize){
        Vector3dInterface newPosition = calculateNewPosition(state.getPosition(),state.getVelocity(), stepSize);
        Vector3dInterface newVelocity = calculateNewVelocity(state.getVelocity(), stepSize);
        double newAngle = calculateNewAngle(state.getAngle(), state.getAngularVelocity(), stepSize);
        double newAngularVelocity = calculateNewAngularVelocity(state.getAngularVelocity(), stepSize);

        return new LanderState(newPosition, newVelocity, newAngle, newAngularVelocity);
    }

    public Vector3dInterface calculateNewPosition(Vector3dInterface position, Vector3dInterface velocity, double stepSize){
        return position.addMul(stepSize, velocity);
    }

    private Vector3dInterface calculateNewVelocity(Vector3dInterface velocity, double stepSize) {
        return velocity.addMul(stepSize, totalForce.mul(1.0/lander.getMASS()));
    }

    private double calculateNewAngle(double angle, double angularVelocity, double stepSize) {
        return angle + angularVelocity*stepSize;
    }

    private double calculateNewAngularVelocity(double angularVelocity, double stepSize) {
        return angularVelocity + stepSize*torque/lander.getMOMENT();
    }






}
