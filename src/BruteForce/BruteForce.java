package BruteForce;

import titan.*;

import java.util.ArrayList;

public class BruteForce {
    private static final Vector3dInterface EarthP = new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06);
    private static final Vector3dInterface EarthV = new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01);
    private static final double EarthR = 6.371e6;
    private static final double minSpeed = 12000;//m/s
    private static final double maxSpeed = 60000;//m/s

    private static final int EARTH_ID = 3;
    private static final int TITAN_ID = 8;

    private static final double RADIUS_TITAN  = 2.5755e6;

    public static double getMinimum(Vector3dInterface[] trajectory, StateInterface[] statesList){
        Vector3dInterface minimum = trajectory[0];
        State initialState = (State)statesList[0];
        int step = 0;
        double minimumDistance = minimum.dist(initialState.getPositionList().get(TITAN_ID));
        for (int i = 1; i < trajectory.length; i++) {
            State temp = (State)statesList[i-1];
            if(trajectory[i].dist(temp.getPositionList().get(TITAN_ID)) < minimumDistance){
                minimum = trajectory[i];
                minimumDistance = trajectory[i].dist(temp.getPositionList().get(TITAN_ID));
                step = i;
            }
        }
        return minimumDistance;

    }

    public static ArrayList<Vector3dInterface> bruteforce (SolarSystem system){
        StateInterface[] states = system.solve(system, system.getState(), 31556926, 1000);

        double distanceBest = 0;
        Vector3dInterface velocityBest = new Vector(0,0,0);
        Vector3dInterface positionBest = new Vector(0,0,0);
        Vector3dInterface unitVectorBest;
        double launchSpeedBest = 0;


        Probe spaceship = new Probe(system, states);

        for(int i = 0; i < 10; i++){
            Vector3dInterface unitVector = VectorTools.randUnitVector();
            Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
            double launchSpeed = Math.random()*(maxSpeed-minSpeed) + minSpeed;
            //System.out.println("Random unit vector: " + unitVector.toString());
            Vector3dInterface velocity = EarthV.addMul(launchSpeed, unitVector);


            //System.out.println("Initial speed: " + velocity.toString());
            Vector3dInterface[] trajectory = spaceship.trajectory(position, velocity, 31556926, 1000);
            //System.out.println(getMinimum(trajectories, states));

            if( i == 0 || getMinimum(trajectory, states) < distanceBest ) {
                distanceBest = getMinimum(trajectory, states);
                velocityBest = velocity;
                positionBest = position;
                unitVectorBest = unitVector;
                launchSpeedBest = launchSpeed;
            }
        }

        double initialDistanceET = system.getState().getPositionList().get(EARTH_ID).dist(system.getState().getPositionList().get(TITAN_ID));

        System.out.println("Starting while loop");
        boolean hit = false;
        while(!hit) {
            System.out.println();

            double randInt = Math.random();
            int randPower = (int) (Math.random()*2 + 1);
            double changeRate = Math.random()*5*(distanceBest/initialDistanceET)*Math.pow(-1, randPower);
            System.out.println("ChangeRate: " + changeRate);


            Vector3dInterface unitVector;
            double launchSpeedNew = velocityBest.norm();

            if(randInt < 0.25) {
                double newX = velocityBest.getX() + velocityBest.getX()*changeRate;
                unitVector = VectorTools.getUnitVector(new Vector(newX, velocityBest.getY(), velocityBest.getZ()));
            } else if(randInt < 0.5) {
                double newY = velocityBest.getY() + velocityBest.getY()*changeRate;
                unitVector = VectorTools.getUnitVector(new Vector(velocityBest.getX(), newY , velocityBest.getZ()));
            } else if (randInt < 0.75){
                double newZ = velocityBest.getZ() + velocityBest.getZ()*changeRate;
                unitVector = VectorTools.getUnitVector(new Vector(velocityBest.getX(), velocityBest.getY(), newZ));
            } else {
                launchSpeedNew = velocityBest.norm() + velocityBest.norm()*changeRate;
                if(launchSpeedNew > maxSpeed) launchSpeedNew = maxSpeed;
                unitVector = VectorTools.getUnitVector(velocityBest);
            }
            Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
            Vector3dInterface velocity = EarthV.addMul(launchSpeedNew, unitVector);

            Vector3dInterface[] trajectory = spaceship.trajectory(position, velocity, 31556926, 1000);

            double shortestDistance = getMinimum(trajectory, states);
            System.out.println("shortest distance: " + shortestDistance);
            System.out.println("shortest so far: " + distanceBest);
            if(  shortestDistance < distanceBest ) {
                distanceBest = shortestDistance;
                velocityBest = velocity;
                positionBest = position;
                unitVectorBest = unitVector;
                launchSpeedBest = launchSpeedNew;
                System.out.println(distanceBest);
            }

            if(shortestDistance < RADIUS_TITAN) {
                System.out.println("OH MY GOD WE HIT IT");
                System.out.println(velocityBest.toString());
                System.out.println(positionBest.toString());
                hit = true;
            }


        }
        //2.5755e6
        return new ArrayList<Vector3dInterface>();
    }
}
