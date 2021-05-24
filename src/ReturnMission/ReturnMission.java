package ReturnMission;

import NewtonRhapson.NewtonRhapson;
import titan.BodyList;
import titan.State;
import titan.Vector;
import titan.Vector3dInterface;

import java.lang.management.ThreadInfo;
import java.util.ArrayList;

public class ReturnMission {
    public final double GRAV_CONSTANT = 6.674E-11;
    final double STEPSIZE = 500;
    final double FINAL_TIME = 300*24*60*60;
    final double PROBE_MASS = BodyList.getBodyList()[11].getMass();
    final double velocityOrbit;
    final double radius = BodyList.getBodyList()[8].getRadius()+200000;
    final double stepsize = 500;
    final Vector3dInterface INITIAL_VELOCITY_ESTIMATE = new Vector(33573.34843239226, -69124.95246248483, -703.6818444149116);
    final ArrayList<Thrust> thrusts = new ArrayList<>();

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

    private double getNumTimeSteps(double v0, double vf){
        double massFuel = ThrustCalculator.getFuelForVelocity(PROBE_MASS, v0, vf);
        double time = ThrustCalculator.convertFuelToTime(massFuel);
        return ThrustCalculator.convertTimeToSteps(time);
    }

    private State adjustTrajectory(State state){
        //get best velocity with at that state
        NewtonRhapson newtonRhapson = new NewtonRhapson(state, FINAL_TIME, STEPSIZE);
        Vector3dInterface bestVelocity = newtonRhapson.findInitialVelocity(state);
        Vector3dInterface probeVelocity = state.getVelocityList().get(11);

        //calculate thrust needed
        double fuel = ThrustCalculator.getFuelForVelocity(PROBE_MASS, probeVelocity.norm(), bestVelocity.norm());
        int timestep = (int)(state.getStateTime()/STEPSIZE)-1;

        thrusts.add(new Thrust(fuel, timestep));

        return state.setVelocityByID(11, bestVelocity);
    }

    private void doMission(){
        //launch
            //adjust Trajectory

        //move

        //orbit around titan

        //go back to earth
    }

    public static void main(String[] args){
        ReturnMission r = new ReturnMission();
        System.out.println(r.getNumTimeSteps(0, r.INITIAL_VELOCITY_ESTIMATE.norm()));

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
