package GA;

import titan.*;

public class Individual {

    private final Vector3dInterface EarthP = new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06);
    private final Vector3dInterface EarthV = new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01);
    private final double EarthR = 6.371e6;
    private final double minSpeed = 12000;//m/s
    private final double maxSpeed = 60000;//m/s


    private Vector3dInterface initPosition;
    private Vector3dInterface initVelocity;

    public Individual(Vector3dInterface initP, Vector3dInterface initV){
        this.initPosition = initP;
        this.initVelocity = initV;
    }

    public Individual(){
        Vector3dInterface unitVector = VectorTools.randUnitVector();
        this.initPosition = EarthP.addMul(EarthR, unitVector);
        double launchSpeed = Math.random()*(maxSpeed-minSpeed) + minSpeed;
        this.initVelocity = EarthV.addMul(launchSpeed, unitVector);
    }

    public double getFitnessValue(Vector3dInterface[] trajectory, StateInterface[] statesList){
        Vector3dInterface minimum = trajectory[0];
        State initialState = (State)statesList[0];
        int step = 0;
        double minimumDistance = minimum.dist(initialState.getPositionList().get(9));
        for (int i = 1; i < trajectory.length; i++) {
            State temp = (State)statesList[i];
            if(trajectory[i].dist(temp.getPositionList().get(9)) < minimumDistance){
                minimum = trajectory[i];
                minimumDistance = trajectory[i].dist(temp.getPositionList().get(9));
                step = i;
            }
        }
        return 0.0;

    }

}
