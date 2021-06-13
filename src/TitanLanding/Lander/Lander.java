package TitanLanding.Lander;

import titan.*;

public class Lander {
    private final double RADIUS = 4; // 4 meters
    private final double MASS = 6000; // 6000 kg
    public LanderState state;

    public Lander(LanderState ogState){
        this.state = ogState;
    }

}
