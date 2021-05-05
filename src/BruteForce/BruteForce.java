package BruteForce;

import titan.*;

import java.util.ArrayList;
import java.util.Random;

public class BruteForce {
    private static final Vector3dInterface EarthP = new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06);
    private static final Vector3dInterface EarthV = new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01);
    private static final double EarthR = 6.371e6;
    private static final double minSpeed = 12000;//m/s
    private static final double maxSpeed = 60000;//m/s
    private static final double STEP_SIZE = 500;
    private static final double TIME_FRAME =  31556926;

    private static final int EARTH_ID = 3;
    private static final int TITAN_ID = 8;

    private static final double RADIUS_TITAN  = 2.5755e6;

    private static Vector3dInterface directionVector;

    /** Takes in a trajectory , an array of states and a planet ID and returns
     *  the distance of the closest point of the trajectory to the planet
     *  the trajectory and statelist need to have the same size and stepSize for accurate results
     * @param trajectory trajectory of a probe
     * @param statesList list of states of the system
     * @param planetID  ID of the planet you want to calculate the distance to
     * @return closest distance of the trajectory to the planet and corresponding step
     */
    public static double[] getMinimum(Vector3dInterface[] trajectory, StateInterface[] statesList, int planetID){
        Vector3dInterface minimum = trajectory[0];
        State initialState = (State)statesList[0];
        int step = 0;
        double minimumDistance = minimum.dist(initialState.getPositionList().get(planetID));
        for (int i = 0; i < trajectory.length -1; i++) {
            State temp = (State)statesList[i];
            if(trajectory[i].dist(temp.getPositionList().get(planetID)) < minimumDistance){
                minimum = trajectory[i];
                minimumDistance = trajectory[i].dist(temp.getPositionList().get(planetID));
                step = i;
            }
        }
        directionVector = VectorTools.directionVector(trajectory[step], ((State) statesList[step]).getPositionList().get(planetID));
        return new double[]{minimumDistance, step};

    }

    public static ArrayList<Vector3dInterface> bruteforce (SolarSystem system){
        double START = System.currentTimeMillis();
        System.out.println("Finding path");
        System.out.println("Step size: " + STEP_SIZE);

        StateInterface[] states = system.solve(system, system.getState(), 31556926, STEP_SIZE);

        double distanceBest = 0;
        double step = 0;
        Vector3dInterface velocityBest = new Vector(0,0,0);
        Vector3dInterface positionBest = new Vector(0,0,0);
        Vector3dInterface unitVectorBest = new Vector(0,0,0);
        Vector3dInterface directionBest= new Vector(0,0,0);
        double launchSpeedBest = 0;


        Probe spaceship = new Probe(system, states);

        for(int i = 0; i < 100; i++){
            Vector3dInterface unitVector = VectorTools.randUnitVector();
            Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
            double launchSpeed = Math.random()*(maxSpeed-minSpeed) + minSpeed;
            Vector3dInterface velocity = unitVector.mul(launchSpeed);
            Vector3dInterface velocityTotal = EarthV.add(velocity);



            Vector3dInterface[] trajectory = spaceship.trajectory(position, velocityTotal, 31556926, STEP_SIZE);
            double[] minArray = getMinimum(trajectory, states, TITAN_ID);
            if( i == 0 || minArray[0] < distanceBest ) {
                distanceBest = minArray[0];
                velocityBest = velocity;
                positionBest = position;
                unitVectorBest = unitVector;
                launchSpeedBest = launchSpeed;
                directionBest = directionVector;
            }
        }

        double initialDistanceET = system.getState().getPositionList().get(EARTH_ID).dist(system.getState().getPositionList().get(TITAN_ID));
        boolean hit = false;           // flag to check if we've reached Titan
        int counter = 0;

        while(!hit) {

            int randPower = (int) (Math.random()*2 + 1); // will either be 1 or 2
            double changeRate = ((new Random().nextInt(9)+1)*(100.0/Math.sqrt(counter))) *(distanceBest/initialDistanceET)*Math.pow(-1, randPower);


            Vector3dInterface unitVector = new Vector(0,0,0);
            double launchSpeedNew = velocityBest.norm();

            double randNumber = Math.random();
            if(randNumber < 0.25) {
                double newX = velocityBest.getX()*(1 + changeRate);
                unitVector = VectorTools.getUnitVector(new Vector(newX, velocityBest.getY(), velocityBest.getZ()));
            } else if(randNumber < 0.5) {
                double newY = velocityBest.getY()*(1 + changeRate);
                unitVector = VectorTools.getUnitVector(new Vector(velocityBest.getX(), newY , velocityBest.getZ()));
            } else if (randNumber < 0.75){
                double newZ = velocityBest.getZ()*(1 + changeRate);
                unitVector = VectorTools.getUnitVector(new Vector(velocityBest.getX(), velocityBest.getY(), newZ));
            } else {
                launchSpeedNew = velocityBest.norm()*(1 + changeRate);
                if(launchSpeedNew > maxSpeed) launchSpeedNew = maxSpeed;
                unitVector = VectorTools.getUnitVector(velocityBest);
            }
            Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
            Vector3dInterface velocity = unitVector.mul(launchSpeedNew);
            Vector3dInterface velocityTotal = EarthV.add(velocity);

            Vector3dInterface[] trajectory = spaceship.trajectory(position, velocityTotal, 31556926, STEP_SIZE);

            double[] minArray = getMinimum(trajectory, states, TITAN_ID);
//            double shortestDistance = minArray[0];
//            System.out.println();
//
//            System.out.println("ChangeRate: " + changeRate);
//            System.out.println("shortest distance: " + shortestDistance);
////            System.out.println("Velocity vector: " + velocity.toString());
//            System.out.println("shortest so far: " + distanceBest);
////            System.out.println("Velocity best: " + velocityBest.toString());
//            System.out.println(directionVector.toString());
//            if(counter%10000 == 0) {
//                System.out.println();
//                System.out.println("ChangeRate: " + changeRate);
//                System.out.println("shortest distance: " + shortestDistance);
//                System.out.println("shortest so far: " + distanceBest);
//                System.out.println("direction best: " + directionBest.toString());
//            }

            if(  minArray[0] < distanceBest ) {
                distanceBest = minArray[0];
                step = minArray[1];
                velocityBest = velocity;
                positionBest = position;
                unitVectorBest = unitVector;
                launchSpeedBest = velocity.norm();
                directionBest = directionVector;
            }

            if(distanceBest < RADIUS_TITAN) {
                System.out.println("OH MY GOD WE HIT IT");
                System.out.println("Distance to Titan's centre: " + distanceBest);
                System.out.println(velocityBest.toString());
                System.out.println(positionBest.toString());
                System.out.println("Step: " + step);
                System.out.println("Travel time: " + step*STEP_SIZE);
                System.out.println("Runtime: " + (System.currentTimeMillis() - START));

                hit = true;
            }

            counter++;
        }

        return new ArrayList<Vector3dInterface>();
    }
}
