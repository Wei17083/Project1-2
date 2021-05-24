package NewtonRhapson;

import titan.*;
import titan.State;
import titan.Vector;
import titan.Vector3dInterface;
import System.SolarSystem;

import java.util.regex.Matcher;

public class NewtonRhapson {

    private State initialState;
    private State initialStateNewAttempt;
    private boolean firstMission = true;
    private Vector3dInterface initialVelocity;
    private State[] statesNewAttempt;
    private State[] statesLastAttempt;
    private Vector3dInterface finalPosition;
    private int stepClosestPositionNewAttempt;

    private double finalTime;
    private double stepSize;

    private final int PROBE_ID = 11;
    private final int TITAN_ID = 8;
    private final int EARTH_ID = 3;

    public NewtonRhapson(State initialState, double finalTime, double stepSize){
        this.initialState = initialState;
        this.finalTime = finalTime;
        this.stepSize = stepSize;
        SolarSystem system = new SolarSystem(BodyList.getBodyList(), initialState);
        statesLastAttempt = system.calculateTrajectories(finalTime, stepSize);
    }

    public Vector3dInterface findInitialVelocity(State initialState, Vector3dInterface finalPosition, double stepSize, double finalTime) {
    initialStateNewAttempt = initialState.setVelocityByID(PROBE_ID, VectorTools.clone(initialState.getVelocityList().get(PROBE_ID)));

        if (!closeEnough()){ //get next
            this.initialVelocity = getNextAttempt();
            initialStateNewAttempt = initialStateNewAttempt.setVelocityByID(PROBE_ID, initialVelocity);
            SolarSystem newSystem = new SolarSystem(BodyList.getBodyList(), initialStateNewAttempt);
            statesNewAttempt = newSystem.calculateTrajectories(finalTime, stepSize);
            setFinalPosition();
        }
        return initialVelocity;
    }

    private Vector3dInterface getNextAttempt(){

        Vector3dInterface distanceFromDestination = calculateDistanceVectorFromDestination(statesLastAttempt); // g(vk)

        Matrix jacobianMatrix = calculateJacobianMatrix();
        Matrix inverseMatrix = jacobianMatrix.inverse();

        Vector3dInterface velocityChange = inverseMatrix.multiplicationMatrixVector(distanceFromDestination);
        Vector3dInterface newVelocity = initialVelocity.sub(velocityChange);
        return newVelocity;
    }

    private Vector3dInterface calculateDistanceVectorFromDestination(State[] stateList){ // basically g(v)
        int destinationPlanetID = getDestinationPlanetID();
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
        return step;
    }

    public Vector3dInterface getDistanceVectorAtStep(State[] stateList, int step, int planet_id) {
        Vector3dInterface positionProbe = stateList[step].getPositionList().get(PROBE_ID);
        Vector3dInterface positionPlanet = stateList[step].getPositionList().get(planet_id);
        return positionPlanet.sub(positionProbe);
    }

    //TODO fix close enough
    private boolean closeEnough() {

        int destinationPlanetID = getDestinationPlanetID();

        Vector3dInterface destinationPlanetPosition = statesNewAttempt[stepClosestPositionNewAttempt].getPositionList().get(destinationPlanetID);

        double distance = finalPosition.dist(destinationPlanetPosition);

        double destinationPlanetRadius = BodyList.getBodyList()[destinationPlanetID].getRadius();

        return (destinationPlanetRadius + 100000) < distance && distance < (destinationPlanetRadius + 300000);
    }

    private void setFinalPosition(){
        int destinationPlanetID = getDestinationPlanetID();
        stepClosestPositionNewAttempt = getStepClosestPosition(statesNewAttempt, destinationPlanetID);
        finalPosition = statesLastAttempt[stepClosestPositionNewAttempt].getPositionList().get(PROBE_ID);
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
        double velocityComponent1 = getVectorComponent(var1, initialVelocity);
        double velocityComponent2 = getVectorComponent(var2, initialVelocity);

        double relativeChange = 0.0001;
        double change = Math.abs(velocityComponent1*relativeChange);

        Body[] bodies = BodyList.getBodyList();

        Vector3dInterface velocityPositiveChange = changeVelocity(var1, change, initialVelocity);
        Vector3dInterface velocityNegativeChange = changeVelocity(var1, -change, initialVelocity);

        State[] stateListPlus = getStatesChangedVelocity(velocityPositiveChange);
        State[] stateListMinus = getStatesChangedVelocity(velocityNegativeChange);

        Vector3dInterface directionVectorPlus = calculateDistanceVectorFromDestination(stateListPlus);
        Vector3dInterface directionVectorMinus = calculateDistanceVectorFromDestination(stateListMinus);

        return (getVectorComponent(var2, directionVectorPlus)-getVectorComponent(var2, directionVectorMinus)/2*change);
    }

    public State[] getStatesChangedVelocity(Vector3dInterface newVelocity){
        State newInitialState = initialState.setVelocityByID(PROBE_ID, newVelocity);
        SolarSystem system = new SolarSystem(BodyList.getBodyList(), newInitialState);
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
}
