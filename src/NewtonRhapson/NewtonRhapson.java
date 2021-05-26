package NewtonRhapson;

import titan.*;
import titan.State;
import titan.Vector;
import titan.Vector3dInterface;
import System.SolarSystem;

import java.util.regex.Matcher;

public class NewtonRhapson {
    private final boolean DEBUG = false;

    private final State initialState;
    private State initialStateNewAttempt;
    private boolean firstMission = true;
    private Vector3dInterface initialVelocity;
    private State[] statesNewAttempt;
    private State[] statesLastAttempt;
    private Vector3dInterface finalPosition;
    private int stepClosestPosition;

    private final Vector3dInterface TARGET;
    private final boolean isFirstMission;

    private final double finalTime;
    private final double stepSize;

    private final int PROBE_ID = 11;
    private final int TITAN_ID = 8;
    private final int EARTH_ID = 3;

    public NewtonRhapson(State initialState, double finalTime, double stepSize, boolean isFirstMission){
        this.initialState = initialState;
        this.finalTime = finalTime;
        this.stepSize = stepSize;
        this.isFirstMission = isFirstMission;

        SolarSystem system = new SolarSystem(initialState);
        statesNewAttempt = system.calculateTrajectories(finalTime, stepSize);
        stepClosestPosition = (int) (finalTime/stepSize);
        TARGET = setTarget();
//        System.out.println("Final position Titan: " + statesNewAttempt[statesNewAttempt.length-1].getPositionList().get(TITAN_ID).toString());
        if(DEBUG) System.out.println("Target: " + TARGET.toString());

        if(DEBUG) System.out.println(stepClosestPosition);
        if(DEBUG) System.out.println(stepClosestPosition);

        double distance = getDistanceVectorAtStep(statesNewAttempt).norm();
        if(DEBUG) System.out.println("Distance to TARGET: " + distance);
    }

    public Vector3dInterface findInitialVelocity(State initialState) {

        initialStateNewAttempt = initialState.setVelocityByID(PROBE_ID, VectorTools.clone(initialState.getVelocityList().get(PROBE_ID)));
        this.initialVelocity = initialState.getVelocityList().get(PROBE_ID);
        if(DEBUG) System.out.println("initial Velocity: " + initialVelocity);

        do { //get next
            this.initialVelocity = getNextAttempt();
            initialStateNewAttempt = initialState.setVelocityByID(PROBE_ID, initialVelocity);
            SolarSystem newSystem = new SolarSystem(initialStateNewAttempt);
            statesNewAttempt = newSystem.calculateTrajectories(finalTime, stepSize);
            setFinalPosition();
            if(DEBUG) System.out.println("New initial velocity: " + initialVelocity.toString());
        } while (!closeEnough());

        return initialVelocity;
    }

    private Vector3dInterface getNextAttempt(){

        Vector3dInterface distanceFromDestination = calculateDistanceVectorFromDestination(statesNewAttempt); // g(vk)

        Matrix jacobianMatrix = calculateJacobianMatrix();
        Matrix inverseMatrix = jacobianMatrix.inverse();

        Vector3dInterface velocityChange = inverseMatrix.multiplicationMatrixVector(distanceFromDestination);
        Vector3dInterface newVelocity = initialVelocity.sub(velocityChange);
        return newVelocity;
    }

    private Vector3dInterface calculateDistanceVectorFromDestination(State[] stateList){ // basically g(v)
        return getDistanceVectorAtStep(stateList);
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
        return step;
    }

    public Vector3dInterface getDistanceVectorAtStep(State[] stateList) {
        Vector3dInterface positionProbe = stateList[stepClosestPosition].getPositionList().get(PROBE_ID);
        return TARGET.sub(positionProbe);
    }


    private boolean closeEnough() {


        double distance = getDistanceVectorAtStep(statesNewAttempt).norm();
        if (DEBUG) System.out.println("Distance to TARGET: " + distance);
        double destinationPlanetRadius = BodyList.getBodyList()[getDestinationPlanetID()].getRadius();

        return distance < 1000;
    }

    private void setFinalPosition(){
        int destinationPlanetID = getDestinationPlanetID();
        //stepClosestPosition = getStepClosestPosition(statesNewAttempt, destinationPlanetID);
        finalPosition = statesNewAttempt[stepClosestPosition].getPositionList().get(PROBE_ID);
    }

    private Matrix calculateJacobianMatrix(){

        double[][] jacobianMatrix = new double[3][3];
        char[] vars = new char[]{'x', 'y', 'z'};

        for(int i = 0; i < jacobianMatrix.length; i++){
            for(int j = 0; j < jacobianMatrix.length; j++){
                jacobianMatrix[i][j] = getPartialDerivative(vars[i], vars[j], this.initialVelocity);
            }
        }

        return new Matrix(jacobianMatrix);
    }

    public void startComingBack(){ this.firstMission = false; }


    /**
     *  Will calculate the partial derivative of var1 to var 2
     * @param var1  first variable, will be slightly changed
     * @param var2
     * @param initialVelocity velocity to calculate the derivative
     * @return
     */
    public double getPartialDerivative(char var1, char var2, Vector3dInterface initialVelocity){
        double velocityComponent1 = getVectorComponent(var2, initialVelocity);

        double relativeChange = 0.0001;
        double change = Math.abs(velocityComponent1*relativeChange);


        Body[] bodies = BodyList.getBodyList();

        Vector3dInterface velocityPositiveChange = changeVelocity(var2, change, initialVelocity);
        Vector3dInterface velocityNegativeChange = changeVelocity(var2, -change, initialVelocity);

        State[] stateListPlus = getStatesChangedVelocity(velocityPositiveChange);
        State[] stateListMinus = getStatesChangedVelocity(velocityNegativeChange);

        Vector3dInterface directionVectorPlus = calculateDistanceVectorFromDestination(stateListPlus);
        Vector3dInterface directionVectorMinus = calculateDistanceVectorFromDestination(stateListMinus);

        double paritalDerivative = ((getVectorComponent(var1, directionVectorPlus)-getVectorComponent(var1, directionVectorMinus))/(2*change));
        return paritalDerivative;
    }

    public State[] getStatesChangedVelocity(Vector3dInterface newVelocity){
        State newInitialState = initialState.setVelocityByID(PROBE_ID, newVelocity);
        SolarSystem system = new SolarSystem( newInitialState);
        return system.calculateTrajectories(finalTime, stepSize);
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

    private int getDestinationPlanetID(){
        if(firstMission)
            return TITAN_ID; // titan ID
        else
            return EARTH_ID; // earth ID
    }

    private Vector3dInterface setTarget() {
        if (isFirstMission) {
            Vector3dInterface zUnitVector = new Vector(0, 0, 1);
            Vector3dInterface finalPositionTitan = statesNewAttempt[statesNewAttempt.length-1].getPositionList().get(TITAN_ID);
            Vector3dInterface startPositionProbe = statesNewAttempt[0].getPositionList().get(PROBE_ID);
            Vector3dInterface startToEndVector = finalPositionTitan.sub(startPositionProbe);

            Vector3dInterface rightAngleVector = VectorTools.crossProduct(startToEndVector, zUnitVector);
            rightAngleVector.setZ(finalPositionTitan.getZ());
            Vector3dInterface rightAngleUnitVector = VectorTools.getUnitVector(rightAngleVector);
            double radiusTitan = BodyList.getBodyList()[TITAN_ID].getRadius();
            double orbitHeight = 200000;

            Vector3dInterface target = finalPositionTitan.add(rightAngleUnitVector.mul(radiusTitan + orbitHeight));

            return target;
        } else {
            return statesNewAttempt[statesNewAttempt.length-1].getPositionList().get(EARTH_ID);
        }
    }
}
