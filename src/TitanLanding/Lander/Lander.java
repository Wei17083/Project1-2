package TitanLanding.Lander;

import titan.*;

public class Lander {
    private final double RADIUS = 4; // 4 meters
    private final double MASS = 6000; // 6000 kg
    private final double MOMENT = 38400;
    private final double forceMainThruster = 16000; //16000 N
    private final double forceSideThrusters = 440; // 440N
    private LanderState state;

    public Lander(LanderState ogState){
        this.state = ogState;
    }

    public double getMASS() {
        return MASS;
    }

    public double getRADIUS() {
        return RADIUS;
    }

    public double getMOMENT() {
        return MOMENT;
    }

    public double getForceMainThruster() {
        return forceMainThruster;
    }

    public double getForceSideThrusters() {
        return forceSideThrusters;
    }

    public LanderState getState(){
        return this.state;
    }

    public void setState(LanderState state) {
        this.state = state;
    }
}
