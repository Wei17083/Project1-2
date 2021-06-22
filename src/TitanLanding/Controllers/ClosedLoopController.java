package TitanLanding.Controllers;

import TitanLanding.Lander.Lander;
import TitanLanding.Lander.LanderState;
import TitanLanding.LandingPhysics.LanderForceCalculator;
import TitanLanding.LandingPhysics.LanderSolver;
import TitanLanding.WindSimulation.WindSimulator;

import titan.Vector;
import titan.Vector3dInterface;

import java.lang.Math;
import java.util.ArrayList;

public class ClosedLoopController {
    private final Lander lander = new Lander(null);
    private final LanderState initialState;
    double gravConst = -1.352;
    private boolean reachable;


    private double thrustRequired;
    private double crossPoint;

    public ClosedLoopController(LanderState initialState) {
        this.initialState = initialState;
    }

    private final boolean WIND = true;

    public static void main(String[] args) {
        Vector3dInterface initialPosition = new Vector(-500, 300000, 0);
        Vector3dInterface initialSpeed = new Vector(0, -100, 0);
        LanderState initialState = new LanderState(initialPosition, initialSpeed, 0, 0);
        Lander lander = new Lander(initialState);

        ClosedLoopController controller = new ClosedLoopController(initialState);
        double stepSize = 0.3;
        ArrayList<LanderState> descentStates = controller.getLanderDescent(stepSize);

        for (LanderState state: descentStates) {
            System.out.println(state.toString());
        }
        System.out.println("Time for landing: " + descentStates.size());

        // Print to copy to excel
//        double count = 0;
//        for (LanderState state: descentStates) {
//            if(count % 10 == 0 || count == descentStates.size()-1) {
//                System.out.println(state.getPosition().getX() +"," +state.getPosition().getY() + "," + state.getVelocity().getX() +"," + state.getVelocity().getY() + "," + state.getAngle() + ","+ state.getAngularVelocity());
//            }
//            count++;
//        }
    }

    public ArrayList<LanderState> getLanderDescent(double stepSize){
        ArrayList<LanderState> descentStates = new ArrayList<>();
        LanderState landerState = initialState;
        descentStates.add(landerState);


        while(landerState.getPosition().getY()>0.01) {
            landerState = descentStates.get(descentStates.size()-1);
//            System.out.println(landerState.toString());
            double torque = calculateTorque(landerState, stepSize);
//            System.out.println("Reachable: " + reachable);
            Vector3dInterface mainThrust = calculateMainThrust(landerState, stepSize, true);
            Vector3dInterface totalForce = mainThrust.add(LanderForceCalculator.calculateGravityForce(lander));
            if(WIND) totalForce.add(LanderForceCalculator.calculateWindForce(new Lander(landerState), WindSimulator.generateSingleValue(landerState.getPosition().getY())));
//            System.out.println("ThrustVector: " + mainThrust.toString());
//            System.out.println("point 1");

//            System.out.println("point 2");
            LanderSolver solver = new LanderSolver(totalForce, torque, lander);
            descentStates.add(solver.stateStep(landerState, stepSize));
            landerState = descentStates.get(descentStates.size()-1);

//            System.out.println();
//            System.out.println();
        }

        return descentStates;
    }


    public Vector3dInterface calculateMainThrust(LanderState landerState, double stepSiz, boolean reachable) {
        getCrossPoint(landerState);

        double verticalAccelerationNeeded = getVerticalAccelerationNeeded(landerState);

        double Yvelocity = landerState.getVelocity().getY();
    //    if(Yvelocity < 0) verticalAccelerationNeeded = Math.min(-landerState.getVelocity().getY()/2, verticalAccelerationNeeded);
//        if(Yvelocity > 0) return new Vector(0,0,0);
        thrustRequired = (verticalAccelerationNeeded-gravConst)*lander.getMASS()/Math.cos(landerState.getAngle())*0.99;

        thrustRequired = Math.max(0, thrustRequired);
//        System.out.println("Acceleration needed: " + verticalAccelerationNeeded);
        Vector3dInterface directionVector = LanderForceCalculator.getThrustDirection(landerState.getAngle());
//        System.out.println(directionVector.toString());
        Vector3dInterface thrustVector = directionVector.mul(thrustRequired);
        return thrustVector;
    }


    public double calculateTorque(LanderState landerState, double stepSize) {
        double angularVelocity = landerState.getAngularVelocity();
        double currentX = landerState.getPosition().getX();
        if (Math.abs(angularVelocity) < 10E-5) {
            double expectedPositionNextCrossPoint = getExpectedPositionNextCrossPoint(landerState, stepSize);
//            System.out.println("current position: " + currentX + "\n" + "ExpectedPosition: " + expectedPositionNextCrossPoint );
            if(thrustRequired != 0 && Math.abs(landerState.getAngle()) < Math.PI/4
                    || -Math.signum(expectedPositionNextCrossPoint)*landerState.getAngle() <= 0) {
                return -Math.signum(expectedPositionNextCrossPoint)*lander.getForceSideThrusters()* lander.getRADIUS();
            }
            else return 0;
        }
        else {
            return -Math.signum(landerState.getAngularVelocity())*lander.getForceSideThrusters()* lander.getRADIUS();
        }
    }



    private double getExpectedPositionNextCrossPoint(LanderState landerState, double stepSize) {
        double crossPoint = getCrossPoint(landerState);
        double originalX = landerState.getPosition().getX();
        while (landerState.getPosition().getY() > crossPoint + 0) {
            LanderSolver solver = new LanderSolver(calculateMainThrust(landerState, stepSize, true).add(LanderForceCalculator.calculateGravityForce(lander)), 0, lander );
            landerState = solver.stateStep(landerState, stepSize);
//            System.out.println("while loop getExpectedPositionNextCrossPoint");
//            System.out.println(landerState.toString());
//            System.out.println(landerState.toString());
        }

        return landerState.getPosition().getX();
    }

    public double getCrossPoint(LanderState landerState) {
        double distanceToCrossPoint = Math.max(landerState.getPosition().getY()/10, 3);

        crossPoint = Math.max(0,crossPoint = landerState.getPosition().getY() - distanceToCrossPoint);
        return crossPoint;
    }

    public double getVerticalAccelerationNeeded(LanderState landerState) {
        double accelerationRequired = -Math.pow(landerState.getVelocity().getY(),2)/(2*(crossPoint - landerState.getPosition().getY()));
        return accelerationRequired;
    }
}
