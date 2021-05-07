package titan;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolarSystem implements ODESolverInterface, ODEFunctionInterface {


        public final double GRAV_CONSTANT = 6.674E-11;
        // One AU is approximately the average distance between the Earth and the Sun
        // value taken from https://cneos.jpl.nasa.gov/glossary/au.html
        public static final double AU = 1.495978707e11;
        private static int maxSteps = 1000;
        private static double stepSize = 0.1;

        private final Body[] bodies;
        private State initialState;

        public SolarSystem(Body[] bodies) {
                this.bodies = bodies;
                ArrayList<Vector3dInterface> positionList = new ArrayList<>();
                ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
                for (Body b : bodies) {
                        positionList.add(b.getPosition());
                        velocityList.add(b.getVelocity());
                }
                this.initialState = new State(0, positionList, velocityList);
        }

        public Body[] getBodies() {
                return bodies;
        }

        public State getState() {
                return initialState;
        }

        /**
         * Calculates the barycenter and changes all vectors to have the barycenter as
         * source
         */
        public void reCenter() {

        }

        /*
         * Solve the differential equation by taking multiple steps.
         *
         * @param f the function defining the differential equation dy/dt=f(t,y)
         *
         * @param y0 the starting state
         *
         * @param ts the times at which the states should be output, with ts[0] being
         * the initial time
         *
         * @return an array of size ts.length with all intermediate states along the
         * path
         */
        @Override
        public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
                double stepSize = ts[1]-ts[0];
                for (int i = 1; i < ts.length; i++){
                        if(ts[i]-ts[i-1] < stepSize){
                                stepSize = ts[i]-ts[i-1];
                        }
                }
                int iterations = (int) (Math.round((ts[ts.length - 1]) / stepSize) + 2);
                StateInterface[] stateList = new StateInterface[ts.length];
                stateList[0] = y0;
                StateInterface state = y0;
                int index = 1;
                for (int i = 1; i < iterations && index < ts.length; i++) {
                        if (Math.abs((i - 1) * stepSize - ts[index]) < Math.abs(i * stepSize - ts[index])) {
                                stateList[index] = state;
                                index++;
                        }
                        state = step(f, stepSize * i, state, stepSize);
                }
                return stateList;
        }

        /*
         * Solve the differential equation by taking multiple steps of equal size,
         * starting at time 0. The final step may have a smaller size, if the step-size
         * does not exactly divide the solution time range
         *
         * @param f the function defining the differential equation dy/dt=f(t,y)
         *
         * @param y0 the starting state
         *
         * @param tf the final time
         *
         * @param h the size of step to be taken
         *
         * @return an array of size round(tf/h)+1 including all intermediate states
         * along the path
         */
        @Override
        public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
                int size = (int) (tf / h + 1);
                StateInterface[] stateList = new StateInterface[size];
                stateList[0] = y0;
                for (int i = 1; i < size-1; i++) {
                        stateList[i] = step(f, h * i, stateList[i - 1], h);
                }
                stateList[size-1] = step(f,tf, stateList[size-2], tf-h*size);
                return stateList;
        }

        /*
         * Update rule for one step.
         *
         * @param f the function defining the differential equation dy/dt=f(t,y)
         *
         * @param t the time
         *
         * @param y the state
         *
         * @param h the step size
         *
         * @return the new state after taking one step
         */
        @Override
        public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
                return y.addMul(h, call(t, y));
        }

        /*
         * This is an interface for the function f that represents the differential
         * equation dy/dt = f(t,y). You need to implement this function to represent to
         * the laws of physics.
         *
         * For example, consider the differential equation dy[0]/dt = y[1];
         * dy[1]/dt=cos(t)-sin(y[0]) Then this function would be f(t,y) =
         * (y[1],cos(t)-sin(y[0])).
         *
         * @param t the time at which to evaluate the function
         *
         * @param y the state at which to evaluate the function
         *
         * @return The average rate-of-change over the time-step. Has dimensions of
         * [state]/[time].
         */
        @Override
        public RateInterface call(double t, StateInterface y) {
                State y1 = (State) y;
                ChangeRate rate = new ChangeRate();
                for (Body b : bodies) {
                        rate.addPositionChange(y1.getVelocityList().get(b.getID()));

                        ArrayList<Vector3dInterface> forces = new ArrayList<>();
                        for (Body b2 : bodies) {
                                if (!(b.equals(b2))) {
                                        forces.add(gravitationalPull(b.getMass(), b2.getMass(), y1.getPositionList().get(b.getID()), y1.getPositionList().get(b2.getID())));
                                }
                        }
                        rate.addVelocityChange(VectorTools.sumAll(forces).mul(1 / b.getMass()));
                }
                return rate;
        }


        public Vector3dInterface gravitationalPull(double mass1, double mass2, Vector3dInterface p1, Vector3dInterface p2) {

                // TODO: Implement physics calculations on force here
                double distance = p2.dist(p1);
                Vector3dInterface forceDirection = VectorTools.directionVector(p1, p2);
                double force = GRAV_CONSTANT * mass1 * mass2 / Math.pow(distance, 2);
                return forceDirection.mul(force);
        }

        public static double getAU() {
                return AU;
        }

}
