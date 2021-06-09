package TitanLanding;

import titan.*;

public class WindValues {
    private int[] altitudes;
    private Vector3dInterface[] speeds;

    public WindValues(int[] altitudes, Vector3dInterface[] speeds) {
        this.altitudes = altitudes;
        this.speeds = speeds;
    }

    public int[] getAltitudes() {
        return altitudes;
    }

    public void setAltitudes(int[] altitudes) {
        this.altitudes = altitudes;
    }

    public Vector3dInterface[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(Vector3dInterface[] speeds) {
        this.speeds = speeds;
    }
}
