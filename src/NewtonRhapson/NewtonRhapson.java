package NewtonRhapson;

import titan.*;
import titan.State;
import titan.Vector;
import titan.Vector3dInterface;
import System.SolarSystem;

public class NewtonRhapson {

    private State initialState;
    private boolean firstMission = true;
    private Vector3dInterface initialVelocity;
    private State[] statesNewAttempt;
    private State[] statesLastAttempt;
    private Vector3dInterface finalPosition;
    private int stepClosestPositionLastAttempt;

    private double finalTime;
    private double stepSize;

    private final int PROBE_ID = 11;
    private final int TITAN_ID = 8;

    public NewtonRhapson(State initialState, double finalTime, double stepSize){
        this.initialState = initialState;
        this.finalTime = finalTime;
        this.stepSize = stepSize;
        SolarSystem system = new SolarSystem(BodyList.getBodyList(), initialState);
        statesLastAttempt = system.calculateTrajectories(finalTime, stepSize);
    }

    public Vector3dInterface findInitialVelocity(State initialState, Vector3dInterface finalPosition, double stepSize, double finalTime) {

        if (!closeEnough()){ //get next
            this.initialVelocity = getNextAttempt();
            finalPosition = getMinDistance();
        }
    }

    private Vector3dInterface getNextAttempt(){

        double oldX = this.initialVelocity.getX();
        double oldY = this.initialVelocity.getY();
        double oldZ = this.initialVelocity.getZ();

        double newX, newY, newZ;

        Vector3dInterface distanceFromDestination = calculateDistanceVectorFromDestination(statesLastAttempt); // g(vk)

        Matrix jacobianMatrix = getJacobianMatrix();

        Vector3dInterface vectorToSub = jacobianMatrix.inverse().mul(distanceFromDestination);

        newX = oldX - vectorToSub.getX();
        newY = oldY - vectorToSub.getY();
        newZ = oldZ - vectorToSub.getZ();

        return new Vector(newX, newY, newZ);
    }

    private Vector3dInterface calculateDistanceVectorFromDestination(State[] stateList){ // basically g(v)

        int destinationPlanetID;
        if(firstMission)
            destinationPlanetID = 8; // titan ID
        else
            destinationPlanetID = 3; // earth ID

        int stepClosestPosition = getStepClosestPosition(stateList, destinationPlanetID);
        return getDistanceVectorAtStep(stateList ,stepClosestPosition, destinationPlanetID);
    }

    public int getStepClosestPosition(State[] statesList, int planetID){
        Vector3dInterface minimum = statesList[0].getPositionList().get(PROBE_ID);
        State initialState = (State)statesList[0];
        int step = 0;
        double minimumDistance = minimum.dist(initialState.getPositionList().get(planetID));
        for (int i = 0; i < statesList.length -1; i++) {
            State temp = statesList[i];
            Vector3dInterface currentPositionProbe = statesList[i].getPositionList().get(PROBE_ID);
            Vector3dInterface currentPositionPlanet = temp.getPositionList().get(planetID);
            if(currentPositionProbe.dist(currentPositionPlanet) < minimumDistance){
                minimum = currentPositionProbe;
                minimumDistance = currentPositionProbe.dist(currentPositionPlanet);
                step = i;
            }
        }
        //directionVector = VectorTools.directionVector(trajectory[step], ((State) statesList[step]).getPositionList().get(planetID));
        return step;
    }

    public Vector3dInterface getDistanceVectorAtStep(State[] stateList, int step, int planet_id) {
        Vector3dInterface positionProbe = stateList[step].getPositionList().get(PROBE_ID);
        Vector3dInterface positionPlanet = stateList[step].getPositionList().get(planet_id);
        return positionPlanet.sub(positionProbe);
    }

    private boolean closeEnough() {

        int destinationPlanetID;
        if(firstMission)
            destinationPlanetID = 8; // titan ID
        else
            destinationPlanetID = 3; // earth ID

        Vector3dInterface destinationPlanetPosition = BodyList.getBodyList()[destinationPlanetID].getPosition();

        double x1 = finalPosition.getX();
        double y1 = finalPosition.getY();
        double z1 = finalPosition.getZ();

        double x2 = destinationPlanetPosition.getX();
        double y2 = destinationPlanetPosition.getY();
        double z2 = destinationPlanetPosition.getZ();

        double squaredDistance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2);
        double distance = Math.sqrt(squaredDistance);

        double destinationPlanetRadius = BodyList.getBodyList()[destinationPlanetID].getRadius();

        if((destinationPlanetRadius+100000) < distance && distance < (destinationPlanetRadius+300000))
            return true;
        return false;
    }

    private Vector3dInterface getMinDistance(){
        // returns position where probe gets given velocity
    }

    private Matrix calculateJacobianMatrix(){
        // returns the jacobian derivative matrix  given Vk
    }

    public void startComingBack(){ this.firstMission = false; }


    /**
     *  Will calculate the partial derivative of var1 to var 2
     * @param var1  first variable, will be slightly changed
     * @param var2
     * @param initialVelocity velocity to calculate the derivative
     * @return
     */
    public double getPartialDerivative(char var1, char var2, Vector3dInterface initialPosition, Vector3dInterface initialVelocity){
        double velocityComponent1 = getVectorComponent(var1, initialVelocity);
        double velocityComponent2 = getVectorComponent(var2, initialVelocity);

        double relativeChange = 0.0001;
        double change = Math.abs(velocityComponent1*relativeChange);

        Body[] bodies = BodyList.getBodyList();

        Vector3dInterface velocityPositiveChange = changeVelocity(var1, change, initialVelocity);
        Vector3dInterface velocityNegativeChange = changeVelocity(var1, -change, initialVelocity);


        SolarSystem system = new SolarSystem(BodyList.getBodyList());

        State[] StatesPlus = probe.trajectoryAbsoluteInitialVelocity(initialPosition,velocityPositiveChange, finalTime, stepSize);
        State[] trajectoryMinus = probe.trajectoryAbsoluteInitialVelocity(initialPosition, velocityNegativeChange, finalTime, stepSize);

        Vector3dInterface directionVectorPlus = calculateDistanceVectorFromDestination(stateList);
        Vector3dInterface directionVectorMinus = calculateDistanceVectorFromDestination(stateList);

        double derivative = (getVectorComponent(var2, directionVectorPlus)-getVectorComponent(var2, directionVectorMinus)/2*change);
        return derivative;
    }

    public double getPartialDerivative(char var1, char var2, Vector3dInterface differenceVectorPlus, Vector3dInterface differenceVectorMinus) {

    }

    private double getVectorComponent(char var, Vector3dInterface initialVelocity) {
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
