package ReturnMission;

import NewtonRhapson.NewtonRhapson;
import titan.*;

import java.io.FileNotFoundException;
import java.lang.management.ThreadInfo;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

import System.SolarSystem;

public class ReturnMission {
    private static int titanApproach=0;
    public final double GRAV_CONSTANT = 6.674E-11;
    final double STEPSIZE = 500;
    final double TIME_TO_TITAN = 300*24*60*60;
    final double TIME_TO_EARTH = TIME_TO_TITAN;
    final double PROBE_MASS = BodyList.getBodyList()[11].getMass();
    final double velocityOrbit;
    final double radius = BodyList.getBodyList()[8].getRadius()+200000;

    final Vector3dInterface INITIAL_VELOCITY_ESTIMATE = new Vector(33573.34843239226, -69124.95246248483, -703.6818444149116);
    final ArrayList<Thrust> thrusts = new ArrayList<>();

    final double NUMBER_OF_ORBITS = 5;

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
        Vector3dInterface probeVelocity = state.getVelocityList().get(PROBE_ID);

        //calculate thrust needed



        int timestep = (int)(state.getStateTime()/STEPSIZE)-1;

        thrusts.add(new Thrust(bestVelocity.sub(probeVelocity).norm(), timestep));

        return state.setVelocityByID(11, bestVelocity);
    }

    private ArrayList<State> doMission(){
        ArrayList<State> statesArrayList = new ArrayList<>();
        //launch
            //adjust Trajectory
        State launchState = createLaunchState();
        boolean isFirstMission = true;
        launchState = adjustTrajectory(launchState, TIME_TO_TITAN, isFirstMission);
        statesArrayList.add(launchState);
        SolarSystem launchSystem = new SolarSystem(launchState);
        addStates(statesArrayList, launchSystem.calculateTrajectories(TIME_TO_TITAN, STEPSIZE));

        //slow down last step to achieve orbital speed
            //get last position
        State finalStatePreOrbit = statesArrayList.get(statesArrayList.size()-1);
        titanApproach = statesArrayList.size()-1; //record time step at which we slow down
        Vector3dInterface finalVelocityWithoutBoost = finalStatePreOrbit.getVelocityList().get(PROBE_ID);

            //calculate required speed at right angle to titan
        Vector3dInterface finalRelativeVelocityAfterBoost = getRightAngleVector(finalStatePreOrbit).mul(velocityOrbit);
        Vector3dInterface finalVelocityAfterBoost = finalRelativeVelocityAfterBoost.add(finalStatePreOrbit.getVelocityList().get(TITAN_ID));
            //calculate fuel
        int timestep = (int)(finalStatePreOrbit.getStateTime()/STEPSIZE)-1;
        thrusts.add(new Thrust(finalVelocityAfterBoost.sub(finalVelocityWithoutBoost).norm(), timestep));
        finalStatePreOrbit = finalStatePreOrbit.setVelocityByID(PROBE_ID, finalVelocityAfterBoost);
        statesArrayList.set(statesArrayList.size()-1,finalStatePreOrbit);

        //orbit around titan
        double stepSizeOrbit = 50;
        int stepsOneAndHalfOrbit = (int) (Math.round(timeOrbit()/stepSizeOrbit)*NUMBER_OF_ORBITS);
        double finalOrbitTime = stepsOneAndHalfOrbit*stepSizeOrbit;


        SolarSystem orbitSystem = new SolarSystem(finalStatePreOrbit);

        State[] orbitStates = orbitSystem.calculateTrajectories(finalOrbitTime, stepSizeOrbit);

//        for (State s: orbitStates
//             ) {
//
//                Vector3dInterface probePosition = s.getPositionList().get(PROBE_ID);
//                Vector3dInterface positionTitan = s.getPositionList().get(TITAN_ID);
//                Vector3dInterface relativePositionProbe = probePosition.sub(positionTitan);
//                System.out.println("Position relative to titan: " + relativePositionProbe);
//                System.out.println("Distance to Titan's surface: " + (relativePositionProbe.norm()-BodyList.getBodyList()[8].getRadius()));
//                System.out.println("Dotproduct relative position and velocity: " + VectorTools.dotProduct(relativePositionProbe, s.getVelocityList().get(PROBE_ID)));
//
//        }
        addStates(statesArrayList, orbitStates);

        //go back to earth

        isFirstMission = false;
        State finalStatePostOrbit = statesArrayList.get(statesArrayList.size()-1);
        finalStatePostOrbit = adjustTrajectory(finalStatePostOrbit,TIME_TO_EARTH, isFirstMission);
        statesArrayList.set(statesArrayList.size()-1, finalStatePostOrbit);
        SolarSystem returnSystem = new SolarSystem(finalStatePostOrbit);
        addStates(statesArrayList, returnSystem.calculateTrajectories(TIME_TO_EARTH, STEPSIZE));


        double totalMass = 0;
        double totalVelocityChange = 0;
        double massAfterThrust = PROBE_MASS;
        ThrustCalculator.setVE(60000);
        for (int i = thrusts.size() -1; i >=0 ; i--) {
            if(i == 0) ThrustCalculator.setVE(4000);
            Thrust thrust = thrusts.get(i);
            thrust.setMassAfterThrust(massAfterThrust);
            thrust.calculateMassUsed();
            massAfterThrust = thrust.massBeforeThrust;
            totalMass += thrust.massUsed;
//            System.out.println("TimeStep this thrust: " + thrust.getTimestepUsed());
//            System.out.println("velocityChange this thrust: " + thrust.getVelocityChange());
            totalVelocityChange += thrust.getVelocityChange();
        }
//        System.out.println("Total velocityChange: " + totalVelocityChange);
//        System.out.println("Initial mass probe: " + thrusts.get(0).massBeforeThrust);
//        System.out.println("final mass probe: " + thrusts.get(thrusts.size()-1).massAfterThrust);
//        System.out.println("Mass difference: " + (thrusts.get(0).massBeforeThrust - thrusts.get(thrusts.size()-1).massAfterThrust));
        System.out.println("Total mass of fuel used: " + totalMass);
        //return statesArrayList.toArray(new State[0]);
        return  statesArrayList;

    }

    public Vector3dInterface getRightAngleVector(State state) {
        Vector3dInterface zUnitVector = new Vector(0, 0, 1);
        Vector3dInterface positionTitan = state.getPositionList().get(TITAN_ID);
        Vector3dInterface probePosition = state.getPositionList().get(PROBE_ID);
        Vector3dInterface titanToProbeVector = positionTitan.sub(probePosition);

        Vector3dInterface rightAngleVector = VectorTools.crossProduct(titanToProbeVector, zUnitVector);
        Vector3dInterface rightAngleUnitVector = VectorTools.getUnitVector(rightAngleVector);

        return rightAngleUnitVector.mul(1);
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

        initialState = initialState.setVelocityByID(11, bodies[3].getVelocity());
        initialState = initialState.setPositionByID(11, initialPosition);
        return initialState;
    }

    public void addStates(ArrayList<State> stateArrayList, State[] states) {
        stateArrayList.addAll(Arrays.asList(states));
    }

    public static void main(String[] args){
        ReturnMission r = new ReturnMission();
        //State[] states = r.doMission();
        ArrayList<State> stateArrayList = r.doMission();
        ArrayList<StateInterface> stateInterfaceArrayList = new ArrayList<>();
        for (int i = 0; i < stateArrayList.size() ; i++) {
            stateInterfaceArrayList.add((StateInterface) stateArrayList.get(i));
        }
         ToolsCSV cvsTool = new ToolsCSV(stateInterfaceArrayList, 12, "Euler" + (int)r.STEPSIZE + "Data" ,"Euler" + (int)r.STEPSIZE + "Trajectory");

        Vector3dInterface[] trajectory = new Vector3dInterface[stateArrayList.size()];
        for (int i = 0; i < stateArrayList.size() ; i++) {
            trajectory[i] = stateArrayList.get(i).getPositionList().get(r.PROBE_ID);
        }

        try {

            cvsTool.createCSV();
            cvsTool.createProbeCSV(trajectory, trajectory.length, titanApproach);
        } catch (FileNotFoundException e) {System.out.println(e);}



//        System.out.println(states[states.length-1].getPositionList().get(3));
//        System.out.println(states[states.length-1].getPositionList().get(11));
        // random comment so i can comit

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
    double velocityChange;
    double massBeforeThrust;
    double massAfterThrust;

    Thrust(double velocityChange, double timestepUsed){
        this.velocityChange = velocityChange;
        this.timestepUsed = timestepUsed;
    }

    public void setMassAfterThrust(double massAfterThrust) {
        this.massAfterThrust = massAfterThrust;
    }

    public void calculateMassUsed(){
        massBeforeThrust = massAfterThrust*Math.exp(velocityChange/ThrustCalculator.getVe());
        massUsed = massBeforeThrust-massAfterThrust;
    }

    public double getVelocityChange() {
        return velocityChange;
    }

    public double getTimestepUsed() {
        return timestepUsed;
    }
}
