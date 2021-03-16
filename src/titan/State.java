package titan;

import java.util.ArrayList;

public class State implements StateInterface {

    //bodyList will store all the bodies of the system with their respective
    // position and velocity at time tState
    ArrayList<Vector3dInterface> positionList;
    ArrayList<Vector3dInterface> velocityList;
    double tState;

    public ArrayList<Vector3dInterface> getPositionList() {
        return positionList;
    }

    public ArrayList<Vector3dInterface> getVelocityList() {
        return velocityList;
    }

    public double gettState() {
        return tState;
    }

    public State(double t0, ArrayList<Vector3dInterface> positionList, ArrayList<Vector3dInterface> velocityList) {
        this.positionList = positionList;
        this.velocityList = velocityList;
        tState = t0;
    }

    /**
     * Update a state to a new state computed by: this + step * rate
     *
     * @param step   The time-step of the update
     * @param rate   The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     * @return The new state after the update. Required to have the same class as 'this'.
     */
    @Override
    public StateInterface addMul(double step, RateInterface rate) {
        ChangeRate rate1 = (ChangeRate) rate;
        ArrayList<Vector3dInterface> newPositions = new ArrayList<>();
        ArrayList<Vector3dInterface> newVelocities = new ArrayList<>();
        for (int i = 0; i < positionList.size(); i++) {
            newPositions.add(i, positionList.get(i).addMul(step, rate1.getPositionChanges().get(i)));
            newVelocities.add(i, velocityList.get(i).addMul(step, rate1.getVelocityChanges().get(i)));
        }

        return new State(tState + step, newPositions, newVelocities);
    }


}

class ChangeRate implements RateInterface {
    ArrayList<Vector3dInterface> positionChanges;
    ArrayList<Vector3dInterface> velocityChanges;


    public ChangeRate() {
        positionChanges = new ArrayList<Vector3dInterface>();
        velocityChanges = new ArrayList<Vector3dInterface>();
    }

    public void addPositionChange(Vector3dInterface v){
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
