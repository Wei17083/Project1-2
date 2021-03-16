package titan;

import java.util.ArrayList;

public class State implements StateInterface, ODESolverInterface, ODEFunctionInterface{

    //bodyList will store all the bodies of the system with their respective
    // position and velocity at time tState
    ArrayList<Body> bodyList;
    double tState;


    public State(ArrayList<Body> bodyList, double t0) {
        this.bodyList = bodyList;
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
        for (int i = 0; i < bodyList.size(); i++) {
            bodyList.get(i).setPosition(bodyList.get(i).getPosition().addMul(step, rate1.getPositionChanges().get(i)));
        }
        return null;
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

    /*
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
        return new StateInterface[0];
    }

    /*
     * Solve the differential equation by taking multiple steps of equal size, starting at time 0.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   tf      the final time
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 including all intermediate states along the path
     */
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
        return new StateInterface[0];
    }

    /*
     * Update rule for one step.
     *
     * @param   f   the function defining the differential equation dy/dt=f(t,y)
     * @param   t   the time
     * @param   y   the state
     * @param   h   the step size
     * @return  the new state after taking one step
     */
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
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
