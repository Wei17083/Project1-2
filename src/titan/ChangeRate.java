package titan;

import java.util.ArrayList;

public class ChangeRate implements RateInterface {
    private final ArrayList<Vector3dInterface> positionChanges;
    private final ArrayList<Vector3dInterface> velocityChanges;


    public ChangeRate() {
        positionChanges = new ArrayList<Vector3dInterface>();
        velocityChanges = new ArrayList<Vector3dInterface>();
    }

    public void addPositionChange(Vector3dInterface v) {
        positionChanges.add(v);
    }

    public void addVelocityChange(Vector3dInterface v) {
        velocityChanges.add(v);
    }

    public ArrayList<Vector3dInterface> getPositionChanges() {
        return positionChanges;
    }

    public ArrayList<Vector3dInterface> getVelocityChanges() {
        return velocityChanges;
    }
}
