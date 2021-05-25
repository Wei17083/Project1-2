package ReturnMission;

import NewtonRhapson.NewtonRhapson;
import titan.*;

import java.lang.management.ThreadInfo;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

import System.SolarSystem;

public class ReturnMission {
    public final double GRAV_CONSTANT = 6.674E-11;
    final double STEPSIZE = 500;
    final double TIME_TO_TITAN = 300*24*60*60;
    final double TIME_TO_EARTH = 2*TIME_TO_TITAN;
    final double PROBE_MASS = BodyList.getBodyList()[11].getMass();
    final double velocityOrbit;
    final double radius = BodyList.getBodyList()[8].getRadius()+200000;
    final double stepsize = 500;
    final Vector3dInterface INITIAL_VELOCITY_ESTIMATE = new Vector(33573.34843239226, -69124.95246248483, -703.6818444149116);
    final ArrayList<Thrust> thrusts = new ArrayList<>();

    final int PROBE_ID = 11;
    final int TITAN_ID = 8;

    public ReturnMission(){
        velocityOrbit = velocityOrbit();
    }
    public double velocityOrbit(){
        return  Math.sqrt((GRAV_CONSTANT* BodyList.getBodyList()[8].getMass())/radius);
    }

    public double timeOrbit(){
        double circumference = 2*Math.PI*radius;
        return circumference/velocityOrbit;
    }


    private State adjustTrajectory(State state, double finalTime, boolean isFirstMission){
        //get best velocity with at that state
        NewtonRhapson newtonRhapson = new NewtonRhapson(state, finalTime, STEPSIZE, isFirstMission);
        Vector3dInterface bestVelocity = newtonRhapson.findInitialVelocity(state);
        Vector3dInterface probeVelocity = state.getVelocityList().get(11);

        //calculate thrust needed
        double fuel = ThrustCalculator.getFuelForVelocity(PROBE_MASS, probeVelocity, bestVelocity);
        int timestep = (int)(state.getStateTime()/STEPSIZE)-1;

        thrusts.add(new Thrust(fuel, timestep));

        return state.setVelocityByID(11, bestVelocity);
    }

    private State[] doMission(){
        ArrayList<State> statesArrayList = new ArrayList<>();
        //launch
            //adjust Trajectory
        State launchState = createLaunchState();
        boolean isFirstMission = true;
        launchState = adjustTrajectory(launchState, TIME_TO_TITAN, isFirstMission);
        statesArrayList.add(launchState);
        SolarSystem launchSystem = new SolarSystem(launchState);
        addStates(statesArrayList, launchSystem.calculateTrajectories(TIME_TO_TITAN, stepsize));

        //slow down last step to achieve orbital speed
            //get last position
        State finalStatePreOrbit = statesArrayList.get(statesArrayList.size()-1);
        Vector3dInterface finalVelocityWithoutBoost = finalStatePreOrbit.getVelocityList().get(PROBE_ID);
            //calculate required speed at right angle to titan
        Vector3dInterface finalVelocityAfterBoost = getRightAngleVector(finalStatePreOrbit).mul(velocityOrbit);
            //calculate fuel
        double fuel = ThrustCalculator.getFuelForVelocity(PROBE_MASS, finalVelocityWithoutBoost, finalVelocityAfterBoost);
        int timestep = (int)(finalStatePreOrbit.getStateTime()/STEPSIZE)-1;
        thrusts.add(new Thrust(fuel, timestep));
        finalStatePreOrbit = finalStatePreOrbit.setVelocityByID(PROBE_ID, finalVelocityAfterBoost);
        statesArrayList.set(statesArrayList.size()-1,finalStatePreOrbit);

        //orbit around titan
        int stepsOneAndHalfOrbit = (int) (Math.round(timeOrbit()/stepsize)*1.5);
        double finalOrbitTime = stepsOneAndHalfOrbit*stepsize;
        SolarSystem orbitSystem = new SolarSystem(finalStatePreOrbit);
        addStates(statesArrayList, orbitSystem.calculateTrajectories(finalOrbitTime, stepsize));

        //go back to earth

        isFirstMission = false;
        State finalStatePostOrbit = statesArrayList.get(statesArrayList.size()-1);
        finalStatePostOrbit = adjustTrajectory(finalStatePostOrbit,TIME_TO_EARTH, isFirstMission);
        statesArrayList.set(statesArrayList.size()-1, finalStatePostOrbit);
        SolarSystem returnSystem = new SolarSystem(finalStatePostOrbit);
        addStates(statesArrayList, returnSystem.calculateTrajectories(TIME_TO_EARTH, stepsize));

        return statesArrayList.toArray(new State[0]);

    }

    public Vector3dInterface getRightAngleVector(State state) {
        Vector3dInterface zUnitVector = new Vector(0, 0, 1);
        Vector3dInterface positionTitan = state.getPositionList().get(TITAN_ID);
        Vector3dInterface probePosition = state.getPositionList().get(PROBE_ID);
        Vector3dInterface titanToProbeVector = positionTitan.sub(probePosition);

        Vector3dInterface rightAngleVector = VectorTools.crossProduct(titanToProbeVector, zUnitVector);
        Vector3dInterface rightAngleUnitVector = VectorTools.getUnitVector(rightAngleVector);

        return rightAngleUnitVector;
    }

    private State createLaunchState(){
        Body[] bodies = BodyList.getBodyList();
        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        for (Body b : bodies) {
            positionList.add(b.getPosition());
            velocityList.add(b.getVelocity());
        }

        State initialState = new State(0, positionList, velocityList);

        double earthRadius = 6.371e6;

        Vector3dInterface initialRelativeVelocity = INITIAL_VELOCITY_ESTIMATE.sub(bodies[3].getVelocity());
        Vector3dInterface initialRelativePosition = VectorTools.getUnitVector(initialRelativeVelocity).mul(earthRadius);

        Vector3dInterface initialPosition = bodies[3].getPosition().add(initialRelativePosition);

        initialState = initialState.setVelocityByID(11, INITIAL_VELOCITY_ESTIMATE);
        initialState = initialState.setPositionByID(11, initialPosition);
        return initialState;
    }

    public void addStates(ArrayList<State> stateArrayList, State[] states) {
        stateArrayList.addAll(Arrays.asList(states));
    }

    public static void main(String[] args){
        ReturnMission r = new ReturnMission();

        /* TODO
            1: calculate launch velocity with correct solver (do Newton rhapson)
            2: have loop or something to handle launch (first x steps)
            3: at certain intervals call newton rhapson and adjust trajectory to reach titan
            4: slow down to orbit titan
            5: orbit titan a few times
            6: Use newton rhapson to find velocity to return to earth
            7: escape from orbit of titan
            8: same as step 3 but with goal Earth
            9: crash into earth
         */
    }
}

class Thrust{
    double massUsed;
    double timestepUsed;

    Thrust(double massUsed, double timestepUsed){
        this.massUsed = massUsed;
        this.timestepUsed = timestepUsed;
    }
}
