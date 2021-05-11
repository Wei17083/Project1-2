package titan;

import java.util.ArrayList;

public class ChangeRate implements RateInterface {
    private final ArrayList<Vector3dInterface> positionChanges;
    private final ArrayList<Vector3dInterface> velocityChanges;


    public ChangeRate() {
        positionChanges = new ArrayList<Vector3dInterface>();
        velocityChanges = new ArrayList<Vector3dInterface>();
    }

    public ChangeRate(ArrayList<Vector3dInterface> positionChanges, ArrayList<Vector3dInterface> velocityChanges ) {
        this.positionChanges = positionChanges;
        this.velocityChanges = velocityChanges;
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

    public ChangeRate mul(double h) {
        ArrayList<Vector3dInterface> newPositionChanges = new ArrayList<>();
        ArrayList<Vector3dInterface> newVelocityChanges = new ArrayList<>();
        for (int i = 0; i <positionChanges.size() ; i++) {
            newPositionChanges.add(positionChanges.get(i).mul(h));
            newVelocityChanges.add(velocityChanges.get(i).mul(h));
        }
        return new ChangeRate(newPositionChanges, newVelocityChanges);
    }
}
