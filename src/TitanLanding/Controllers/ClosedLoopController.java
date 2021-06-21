package TitanLanding.Controllers;

import TitanLanding.Lander.Lander;
import TitanLanding.Lander.LanderState;
import TitanLanding.LandingPhysics.LanderForceCalculator;
import TitanLanding.LandingPhysics.LanderSolver;
import titan.Vector3dInterface;

import java.lang.Math;

public class ClosedLoopController {
    private final Lander lander = new Lander(null);
    private final LanderState initialState;
    private final double initialHeight;
    private double crossPoint;

    public ClosedLoopController(LanderState initialState) {
        this.initialState = initialState;
        this.initialHeight = initialState.getPosition().getY();
    }


    public Vector3dInterface calculateMainThrust(LanderState landerState, double stepSize) {
        double verticalAccelerationNeeded = getVerticalAccelerationNeeded(landerState);
        double thrustRequired = verticalAccelerationNeeded*lander.getMASS()/Math.cos(landerState.getAngle());
        Vector3dInterface directionVector = LanderForceCalculator.getThrustDirection(landerState.getAngle());
        Vector3dInterface thrustVector = directionVector.mul(thrustRequired);
        return thrustVector;
    }


    public double calculateTorque(LanderState landerState, double stepSize) {
        double angularVelocity = landerState.getAngularVelocity();
        if (Math.abs(angularVelocity) < 10E-10) {
            double expectedPositionNextCrossPoint = getExpectedPositionNextCrossPoint(landerState, stepSize);
            return -Math.signum(expectedPositionNextCrossPoint)*lander.getForceSideThrusters()* lander.getRADIUS()/ lander.getMOMENT();
        }
        else {
            return -Math.signum(landerState.getAngularVelocity())*lander.getForceSideThrusters()* lander.getRADIUS()/ lander.getMOMENT();
        }
    }

    private double getExpectedPositionNextCrossPoint(LanderState landerState, double stepSize) {
        double crossPoint = getCrossPoint(landerState);
        while (landerState.getPosition().getY() > crossPoint) {
            LanderSolver solver = new LanderSolver(calculateMainThrust(landerState, stepSize), 0, lander );
            landerState = solver.stateStep(landerState, stepSize);
        }
        return landerState.getPosition().getX();
    }

    public double getCrossPoint(LanderState landerState) {
        double distanceToCrossPoint = Math.max(landerState.getPosition().getY()/10, 5);
        crossPoint = landerState.getPosition().getY() - distanceToCrossPoint
        return crossPoint;
    }

    public double getVerticalAccelerationNeeded(LanderState landerState) {
        double forceRequired = -Math.pow(landerState.getVelocity().getY(),2)/(2*(crossPoint - landerState.getPosition().getY()));
        if (landerState.getPosition().getY()-crossPoint < 2) forceRequired *= 0.9;
        return forceRequired;
    }
}
