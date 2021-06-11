package TitanLanding;

import titan.*;

public class WindValues {
    private double[] altitudes;
    private Vector3dInterface[] speeds;

    public WindValues(double[] altitudes, Vector3dInterface[] speeds) {
        this.altitudes = altitudes;
        this.speeds = speeds;
    }

    public double[] getAltitudes() {
        return altitudes;
    }

    public void setAltitudes(double[] altitudes) {
        this.altitudes = altitudes;
    }

    public Vector3dInterface[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(Vector3dInterface[] speeds) {
        this.speeds = speeds;
    }

    /**
     * this method gets the windspeed at the given altitude
     * @param altitude the altitude at which we want to get the speed (in m)
     * @return a vector with the windspeed in m/s
     */
    public Vector3dInterface getWindSpeed(double altitude){
        for (int i = 0; i < altitudes.length; i++) {
            if(altitude>altitudes[i]) return speeds[i];
        }
        return null;
    }
}
