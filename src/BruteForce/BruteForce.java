package BruteForce;

import titan.*;

import java.util.ArrayList;

public class BruteForce {
    private static final Vector3dInterface EarthP = new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06);
    private static final Vector3dInterface EarthV = new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01);
    private static final double EarthR = 6.371e6;
    private static final double minSpeed = 12000;//m/s
    private static final double maxSpeed = 60000;//m/s

    public static double getMinimum(Vector3dInterface[] trajectory, StateInterface[] statesList){
        Vector3dInterface minimum = trajectory[0];
        State initialState = (State)statesList[0];
        int step = 0;
        double minimumDistance = minimum.dist(initialState.getPositionList().get(9));
        for (int i = 1; i < trajectory.length; i++) {
            State temp = (State)statesList[i-1];
            if(trajectory[i].dist(temp.getPositionList().get(9)) < minimumDistance){
                minimum = trajectory[i];
                minimumDistance = trajectory[i].dist(temp.getPositionList().get(9));
                step = i;
            }
        }
        return minimumDistance;

    }

    public static ArrayList<Vector3dInterface> bruteforce (SolarSystem system){
        StateInterface[] states = system.solve(system, system.getState(), 31556926, 1000);
        for(int i = 0; i < 10000; i++){
            Vector3dInterface unitVector = VectorTools.randUnitVector();
            Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
            double launchSpeed = Math.random()*(maxSpeed-minSpeed) + minSpeed;
            Vector3dInterface velocity = EarthV.addMul(launchSpeed, unitVector);
            Probe spaceship = new Probe(system, states);
            Vector3dInterface[] trajectories = spaceship.trajectory(position, velocity, 31556926, 1000);

            if(getMinimum(trajectories, states) < 2.5755e6) {
                System.out.println("hit");
            }
        }

        return new ArrayList<Vector3dInterface>();
    }
}
