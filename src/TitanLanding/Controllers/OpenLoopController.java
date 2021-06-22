package TitanLanding.Controllers;

import TitanLanding.Lander.Lander;
import TitanLanding.LandingPhysics.LanderForceCalculator;
import TitanLanding.WindSimulation.*;
import titan.Vector;
import titan.Vector3dInterface;

import java.util.ArrayList;

public class OpenLoopController {

    private final Lander lander;

    private double timeStep;
    private final double BestSpeed = 5;// m/s

    private final Vector3dInterface[] windSpeeds;
    private final double[] altitudes;
    private final Vector3dInterface landingPadPos;

    private final ArrayList<Double> mainThrusts;
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

            double distance = (destX - landerX)/2; // half the distance to accelerate, half to decelerate
            double acc = (2*distance)/(timeStep*timeStep);
            double forceNorm = lander.getMASS() * acc;

            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface verticalAxis = new Vector(0, -1, 0);

            Vector3dInterface thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrusterVector2 = thrusterVector.add(new Vector(forceNorm, 0, 0));

            double angle = -1*getAngle(thrusterVector2, verticalAxis);

            rotations.add(angle);
            mainThrusts.add(thrusterVector2.norm());

            double velocity = acc * timeStep;
            double stopForceNorm = lander.getMASS() * (velocity/(timeStep));
            Vector3dInterface thrustVectorStop = thrusterVector.add(new Vector(-1*stopForceNorm, 0, 0));

            double angleStop = getAngle(thrusterVector2, verticalAxis);

            rotations.add(angleStop);
            mainThrusts.add(thrustVectorStop.norm());
        }
        else if(landerX > destX){
            double distance = (landerX - destX)/2;
            double acc = (2*distance)/(timeStep*timeStep);
            double forceNorm = lander.getMASS() * acc;

            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface verticalAxis = new Vector(0, -1, 0);

            Vector3dInterface thrusterVector = new Vector(-1*resultant.getX(), -1*resultant.getY(), 0);
            Vector3dInterface thrusterVector2 = thrusterVector.add(new Vector(forceNorm, 0, 0));

            double angle = getAngle(thrusterVector2, verticalAxis);

            rotations.add(angle);
            mainThrusts.add(thrusterVector2.norm());

            double velocity = acc * timeStep;
            double stopForceNorm = lander.getMASS() * (velocity/(timeStep));
            Vector3dInterface thrustVectorStop = thrusterVector.add(new Vector(stopForceNorm, 0, 0));

            double angleStop = -1*getAngle(thrusterVector2, verticalAxis);

            rotations.add(angleStop);
            mainThrusts.add(thrustVectorStop.norm());
        }
    }

    private void goDownToGetVelocity(){

        Vector3dInterface verticalAxis = new Vector(0, -1, 0);
        Vector3dInterface windForce = getWindAtCurrentPosition();
        Vector3dInterface nullifyForce = new Vector(-1*windForce.getX(), -1*windForce.getY(), 0);
        double angle = getAngle(nullifyForce, verticalAxis);
        if(getWindDirection())
            angle = -1*angle;

        for(int i = 0; i < 4; i++){
            rotations.add(angle);
            mainThrusts.add(nullifyForce.norm());
        }
    }

    private void goDownKeepingSameVelocity(){
        Vector3dInterface verticalAxis = new Vector(0, -1, 0);
        while(lander.getState().getPosition().getY() > 0){

            Vector3dInterface resultant = getResultantForce();
            Vector3dInterface nullForce = new Vector(-1* resultant.getX(), -1* resultant.getY(), 0);
            double angle = getAngle(nullForce,verticalAxis);
            if(getWindDirection())
                angle = -1*angle;

            rotations.add(angle);
            mainThrusts.add(nullForce.norm());
        }
    }

    private Vector3dInterface getResultantForce(){

        Vector3dInterface gravForce = LanderForceCalculator.calculateGravityForce(this.lander);
        Vector3dInterface windForce = LanderForceCalculator.calculateWindForce(this.lander, getWindAtCurrentPosition());

        return gravForce.add(windForce);
    }

    private Vector3dInterface getWindAtCurrentPosition(){
        double landerAltitude = lander.getState().getPosition().getY();
        for(int i = 0; i < altitudes.length; i++) {
            if(landerAltitude < altitudes[i])
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

    public ArrayList<Double> getMainThrusts(){return this.mainThrusts;}

    public ArrayList<Double> getRotations(){return this.rotations;}
}
