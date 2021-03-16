package titan;

import java.util.ArrayList;

public class State implements StateInterface, ODEFunctionInterface{

    //bodyList will store all the bodies of the system with their respective
    // position and velocity at time tState
    ArrayList<Vector3dInterface> positionList;
    ArrayList<Vector3dInterface> velocityList;
    double tState;


    public State( double t0, ArrayList<Vector3dInterface> positionList, ArrayList<Vector3dInterface> velocityList) {
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
        for (int i = 0; i < positionList.size(); i++) {
            positionList.set(i, positionList.get(i).addMul(step, rate1.getPositionChanges().get(i)));
            velocityList.set(i, velocityList.get(i).addMul(step, rate1.getVelocityChanges().get(i)));
        }

        return this;
    }

    /*
     * This is an interface for the function f that represents the
     * differential equation dy/dt = f(t,y).
     * You need to implement this function to represent to the laws of physics.
     *
     * For example, consider the differential equation
     *   dy[0]/dt = y[1];  dy[1]/dt=cos(t)-sin(y[0])
     * Then this function would be
     *   f(t,y) = (y[1],cos(t)-sin(y[0])).
     *
     * @param   t   the time at which to evaluate the function
     * @param   y   the state at which to evaluate the function
     * @return  The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     */
    @Override
    public RateInterface call(double t, StateInterface y) {
        return null;
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
