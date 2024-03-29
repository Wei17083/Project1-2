package titan;

import java.util.ArrayList;

public class State implements StateInterface {

    //bodyList will store all the bodies of the system with their respective
    // position and velocity at time tState
    ArrayList<Vector3dInterface> positionList;
    ArrayList<Vector3dInterface> velocityList;

    double stateTime;

    public ArrayList<Vector3dInterface> getPositionList() {
        return positionList;
    }

    public ArrayList<Vector3dInterface> getVelocityList() {
        return velocityList;
    }



    public double getStateTime() {
        return stateTime;
    }

    public State(double t0, ArrayList<Vector3dInterface> positionList, ArrayList<Vector3dInterface> velocityList) {
        this.positionList = positionList;
        this.velocityList = velocityList;
        stateTime = t0;
    }

    /**
     * Update a state to a new state computed by: this + step * rate
     *
     * @param step   The time-step of the update
     * @param rate   The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     * @return The new state after the update. Required to have the same class as 'this'.
     */
    @Override
    public State addMul(double step, RateInterface rate) {
        ChangeRate rate1 = (ChangeRate) rate;
        ArrayList<Vector3dInterface> newPositions = new ArrayList<>();
        ArrayList<Vector3dInterface> newVelocities = new ArrayList<>();
        for (int i = 0; i < positionList.size(); i++) {
            newPositions.add(i, positionList.get(i).addMul(step, rate1.getPositionChanges().get(i)));
            newVelocities.add(i, velocityList.get(i).addMul(step, rate1.getVelocityChanges().get(i)));
        }

        return new State(stateTime + step, newPositions, newVelocities);
    }

    public State add(double step, ChangeRate rate) {
        State returnState = this.addMul(1, rate);
        returnState.setStateTime(stateTime+step);
        return  returnState;
    }

    public void setStateTime(double time) {
        stateTime = time;
    }

    public String toString() {
        StringBuilder returnString = new StringBuilder("Positions: ");
        for (Vector3dInterface v: positionList) {
            returnString.append(v.toString()).append(", ");
        }


        returnString.append("\nVelocities:  ");
        for (Vector3dInterface v: velocityList) {
            returnString.append(v.toString()).append(", ");
        }
        return returnString.toString();
    }

    public State setVelocityByID(int id, Vector3dInterface newVelocity){
        ArrayList<Vector3dInterface> newPositionList = new ArrayList<>();
        ArrayList<Vector3dInterface> newVelocityList = new ArrayList<>();
        for (int i = 0; i <positionList.size() ; i++) {
            if(i == id) {
                newVelocityList.add(newVelocity);
            } else {
                newVelocityList.add(VectorTools.clone(velocityList.get(i)));
            }
            newPositionList.add(VectorTools.clone(positionList.get(i)));

        }
        return new State(stateTime, newPositionList, newVelocityList);
    }

    public State setPositionByID(int id, Vector3dInterface newPosition){
        ArrayList<Vector3dInterface> newPositionList = new ArrayList<>();
        ArrayList<Vector3dInterface> newVelocityList = new ArrayList<>();
        for (int i = 0; i <positionList.size() ; i++) {
            if(i == id) {
                newPositionList.add(newPosition);
            } else {
                newPositionList.add(VectorTools.clone(positionList.get(i)));
            }
            newVelocityList.add(VectorTools.clone(velocityList.get(i)));
        }
        return new State(stateTime, newPositionList, newVelocityList);
    }


}

