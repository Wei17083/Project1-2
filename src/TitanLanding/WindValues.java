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
}
