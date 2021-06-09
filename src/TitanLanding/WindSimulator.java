package TitanLanding;

import titan.*;

public class WindSimulator {
    // list of altitude ranges; [i-1] < altitude <= [i]
    private final int[] baseAltitudes = {200,100,0};
    // list of corresponding wind speed vectors
    private final Vector3dInterface[] baseSpeeds = {new Vector(200, 0, 0), new Vector(100, 0, 0), new Vector(0, 0, 0)};
    // determines how big the range of distribution is; range = x +- (x*variation)
    private final double variation = 0.1;

    /**
     * returns the expected wind speeds
     * @return a WindValues object with base (norm) values
     */
    public WindValues getBaseValues(){
        return new WindValues(baseAltitudes, baseSpeeds);
    }

    /**
     * generates stochastic values for the wind speeds based on the variation
     * @return a WindValues object with random values
     */
    public WindValues generateWindValues(){
        return null;
    }

    /**
     * generates the random wind speed for a certain altitude
     * @param altitude the altitude of the wind
     * @return the strength and direction of the wind
     */
    public Vector3dInterface generateSingleValue(int altitude){
        return null;
    }
}
