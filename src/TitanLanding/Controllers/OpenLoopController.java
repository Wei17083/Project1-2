package TitanLanding.Controllers;

import TitanLanding.Lander.Lander;
import TitanLanding.Lander.LanderState;
import TitanLanding.LandingPhysics.LanderForceCalculator;
import TitanLanding.LandingPhysics.LanderSolver;
import TitanLanding.WindSimulation.*;
import titan.Vector;
import titan.Vector3dInterface;

import java.util.ArrayList;

public class OpenLoopController {

    private Lander lander;

    private double timeStep;
    private final double BestSpeed = 5;// m/s

    private Vector3dInterface[] windSpeeds;
    private final double[] altitudes;
    private final Vector3dInterface landingPadPos;

    private final ArrayList<Vector3dInterface> mainThrusts;
    private final ArrayList<Double> rotations;

    public OpenLoopController(Lander lander, Vector3dInterface landingPadPos){
        this.timeStep = 1;
        this.lander = lander;
        this.landingPadPos = landingPadPos;
        WindValues w = TitanLanding.WindSimulation.WindSimulator.getBaseValues();
        this.windSpeeds = w.getSpeeds();
        this.altitudes = w.getAltitudes();
        this.mainThrusts = new ArrayList<>();
        this.rotations = new ArrayList<>();
    }

    public void openControl(){
        goOnTopOfLandingSpace();
        goDownToGetVelocity();
        goDownKeepingSameVelocity();
    }

    private void goOnTopOfLandingSpace(){

        double destX = landingPadPos.getX();
        double landerX = lander.getState().getPosition().getX();

        if(landerX < destX){

            //1
            double distance = (destX - landerX)/2;
            double acc = (2*distance)/(timeStep*timeStep);
            double forceNorm = lander.getMASS() * acc;

            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface verticalAxis = new Vector(0, -1, 0);

            Vector3dInterface thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrusterVector2 = thrusterVector.add(new Vector(forceNorm, 0, 0));

            double angle = -1*getAngle(thrusterVector2, verticalAxis);
            rotations.add(angle);
            mainThrusts.add(thrusterVector2);
            updateState(thrusterVector2);

            //2
            double acc2 = (-1)*(2*distance)/(timeStep*timeStep);
            double stopForceNorm = acc2 * this.lander.getMASS();

            resultant = getResultantForce();
            thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrustVectorStop = thrusterVector.add(new Vector(stopForceNorm, 0, 0));

            double angleStop = getAngle(thrusterVector2, verticalAxis);

            rotations.add(angleStop);
            mainThrusts.add(thrustVectorStop);
            updateState(thrustVectorStop);
            rotations.add(0.0);
            mainThrusts.add(thrusterVector);

        }
        else if(landerX > destX){

            //1
            double distance = (landerX - destX)/2;
            double acc = (2*distance)/(timeStep*timeStep);
            double forceNorm = lander.getMASS() * acc;

            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface verticalAxis = new Vector(0, -1, 0);

            Vector3dInterface thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrusterVector2 = thrusterVector.add(new Vector(forceNorm, 0, 0));

            double angle = getAngle(thrusterVector2, verticalAxis);

            rotations.add(angle);
            mainThrusts.add(thrusterVector2);
            updateState(thrusterVector2);

            //2
            double acc2 = (-1)*(2*distance)/(timeStep*timeStep);
            double stopForceNorm = acc2 * this.lander.getMASS();

            resultant = getResultantForce();
            thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrustVectorStop = thrusterVector.add(new Vector(stopForceNorm, 0, 0));


            double angleStop = -1*getAngle(thrusterVector2, verticalAxis);

            rotations.add(angleStop);
            mainThrusts.add(thrustVectorStop);
            updateState(thrustVectorStop);
            rotations.add(0.0);
            mainThrusts.add(thrusterVector);

        }
    }

    private void goDownToGetVelocity(){
        Vector3dInterface verticalAxis = new Vector(0, -1, 0);
        Vector3dInterface windForce = getWindAtCurrentPosition();
        Vector3dInterface nullifyForce = new Vector(-1*windForce.getX(), -1*windForce.getY(), 0);

        for(int i = 0; i < 4; i++){
            windForce = getWindForce();
            nullifyForce = new Vector(-1*windForce.getX(), -1*windForce.getY(), 0);
            double angle = getAngle(nullifyForce, verticalAxis);
            if(getWindDirection())
                angle = -1*angle;
            rotations.add(angle);
            mainThrusts.add(nullifyForce);
            updateState(nullifyForce);
        }
    }

    private void goDownKeepingSameVelocity(){
        Vector3dInterface verticalAxis = new Vector(0, -1, 0);

        int count = 1;
        while(lander.getState().getPosition().getY() > 0){
            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface nullForce = new Vector(-1* resultant.getX(), -1* resultant.getY(), 0);
            double angle = getAngle(nullForce,verticalAxis);
            if(getWindDirection())
                angle = -1*angle;

            rotations.add(angle);
            mainThrusts.add(nullForce);
            updateState(nullForce);

        }
    }

    private Vector3dInterface getResultantForce(){

        Vector3dInterface gravForce = LanderForceCalculator.calculateGravityForce(this.lander);
        Vector3dInterface windForce = LanderForceCalculator.calculateWindForce(this.lander, getWindAtCurrentPosition());
        return gravForce.add(windForce);
    }

    private Vector3dInterface getWindForce(){
        return LanderForceCalculator.calculateWindForce(this.lander, getWindAtCurrentPosition());
    }


    private Vector3dInterface getWindAtCurrentPosition(){
        double landerAltitude = lander.getState().getPosition().getY();
        for(int i = 0; i < altitudes.length; i++) {
            if(landerAltitude <= altitudes[i])
                return windSpeeds[i];
        }
        return null;
    }

    private double getAngle(Vector3dInterface force1, Vector3dInterface force2){

        double x1 = force1.getX();
        double y1 = force1.getY();
        double x2 = force2.getX();
        double y2 = force2.getY();

        double angle = Math.acos((x1*x2 + y1*y2)/( Math.sqrt(x1*x1+y1*y1) * Math.sqrt(x2*x2 + y2*y2) ));

        return Math.toDegrees(angle);
    }

    private boolean getWindDirection(){ //true if going right
        double landerAltitude = lander.getState().getPosition().getY();
        for(int i = 0; i < altitudes.length; i++) {
            if(landerAltitude < altitudes[i])
                return windSpeeds[i].getX() >= 0;
        }
        return false;
    }

    public void setTimeStep(double t){this.timeStep = t;}

    public void setActualWindSpeeds(){
        this.windSpeeds = WindSimulator.generateWindValues().getSpeeds();
    }

    public ArrayList<Vector3dInterface> getMainThrusts(){return this.mainThrusts;}

    public ArrayList<Double> getRotations(){return this.rotations;}

    private void updateState(Vector3dInterface thrust){

        Vector3dInterface forces = getResultantForce().add(thrust);
        LanderSolver solver = new LanderSolver(forces, 0, this.lander);
        LanderState state = solver.stateStep(this.lander.getState(), 1);
        this.lander.setState(state);

    }

    private void printForce(Vector3dInterface force, String name){
        System.out.print(name + " = ");
        System.out.print(force.getX()+", ");
        System.out.println(force.getY());
    }

    private void setLander(Lander l){this.lander = l;}

    public static void main(String[] args) {
        Vector3dInterface initialPosition = new Vector(-500, 200000, 0);
        Vector3dInterface landingPosition = new Vector(0, 0, 0);
        Vector3dInterface initialSpeed = new Vector(0, 0, 0);
        LanderState initialState = new LanderState(initialPosition, initialSpeed, 0, 0);
        Lander zeLander = new Lander(initialState);

        OpenLoopController controller = new OpenLoopController(zeLander, landingPosition);
        controller.openControl();

        LanderState newInitState = new LanderState(initialPosition, initialSpeed, 0, 0);
        Lander newLander = new Lander(newInitState);
        controller.setLander(newLander);
        controller.setActualWindSpeeds();

        for(Vector3dInterface thrust : controller.mainThrusts) {
            controller.updateState(thrust);
        }
    }
}
