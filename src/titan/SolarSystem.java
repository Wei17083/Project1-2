package titan;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolarSystem implements ODESolverInterface, ODEFunctionInterface {

        public static void main(String[] args) throws InterruptedException {
                // radius taken from
                // https://solarsystem.nasa.gov/solar-system
                // other values taken from solar_system_data-2020_04_01.txt

                Body sun = new Body("Sun", 1, 1.988500e30,
                                new Vector(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06),
                                new Vector(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01),
                                6.95508e8, new Color(249, 249, 127)); // yellow+white

                Body mercury = new Body("Mercury", 2, 3.302e23,
                                new Vector(6.047855986424127e+06, -6.801800047868888e+10, -5.702742359714534e+09),
                                new Vector(3.892585189044652e+04, 2.978342247012996e+03, -3.327964151414740e+03),
                                2.4397e6, new Color(145, 162, 171)); // grey

                Body venus = new Body("Venus", 3, 4.8685e24,
                                new Vector(-9.435345478592035e+10, 5.350359551033670e+10, 6.131453014410347e+09),
                                new Vector(-1.726404287724406e+04, -3.073432518238123e+04, 5.741783385280979e-04),
                                6.0518e6, new Color(181, 92, 69)); // red+brown+grey

                Body earth = new Body("Earth", 4, 5.97219e24,
                                new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06),
                                new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01),
                                6.371e6, new Color(114, 213, 190)); // green+blue+grey

                Body moon = new Body("Moon", 5, 7.349e22,
                                new Vector(-1.472343904597218e+11, -2.822578361503422e+10, 1.052790970065631e+07),
                                new Vector(4.433121605215677e+03, -2.948453614110320e+04, 8.896598225322805e+01),
                                1.7375e6, Color.gray);

                Body mars = new Body("Mars", 6, 6.4171e23,
                                new Vector(-3.615638921529161e+10, -2.167633037046744e+11, -3.687670305939779e+09),
                                new Vector(2.481551975121696e+04, -1.816368005464070e+03, -6.467321619018108e+02),
                                3.3895e6, new Color(171, 62, 41)); // red+brown+dark

                Body jupiter = new Body("Jupiter", 7, 1.89813e27,
                                new Vector(1.781303138592153e+11, -7.551118436250277e+11, -8.532838524802327e+08),
                                new Vector(1.255852555185220e+04, 3.622680192790968e+03, -2.958620380112444e+02),
                                6.9911e7, new Color(215, 148, 61)); // orange+brown

                Body saturn = new Body("Saturn", 8, 5.6834e26,
                                new Vector(6.328646641500651e+11, -1.358172804527507e+12, -1.578520137930810e+09),
                                new Vector(8.220842186554890e+03, 4.052137378979608e+03, -3.976224719266916e+02),
                                5.8232e7, new Color(208, 180, 40)); // gold+brown

                Body titan = new Body("Titan", 9, 1.34553e23,
                                new Vector(6.332873118527889e+11, -1.357175556995868e+12, -2.134637041453660e+09),
                                new Vector(3.056877965721629e+03, 6.125612956428791e+03, -9.523587380845593e+02),
                                2.5755e6, new Color(163, 191, 89)); // yellow+green

                Body uranus = new Body("Uranus", 10, 8.6813e25,
                                new Vector(2.395195786685187e+12, 1.744450959214586e+12, -2.455116324031639e+10),
                                new Vector(-4.059468635313243e+03, 5.187467354884825e+03, 7.182516236837899e+01),
                                2.5362e7, new Color(121, 227, 235)); // blue+green

                Body neptune = new Body("Neptune", 11, 1.02413e26,
                                new Vector(4.382692942729203e+12, -9.093501655486243e+11, -8.227728929479486e+10),
                                new Vector(1.068410720964204e+03, 5.354959501569486e+03, -1.343918199987533e+02),
                                2.4622e7, Color.blue);

                Body probe = new Body("Probe", 12, 1,
                                new Vector(0, 0, 0),
                                new Vector(0, 0, 0),
                                1, null);  


                // create arrays of bodies and corresponding forces
                Body[] bodies = new Body[] { sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, uranus,
                                neptune, probe};

                // use testPhysics.java to run visuals until we have full list of positions
                GUI.visualise(bodies, null);

        }

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
                double stepSize = 100;
                int iterations = (int) (Math.round((ts[ts.length - 1]) / stepSize) + 1);
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
                int size = (int) Math.round(tf / h + 1);
                StateInterface[] stateList = new StateInterface[size];
                stateList[0] = y0;
                for (int i = 1; i < stateList.length; i++) {
                        // System.out.println();
                        // System.out.println();
                        // System.out.println("Step: " + i);
                        stateList[i] = step(f, h * i, stateList[i - 1], h);
                }
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
                                        forces.add(gravitationalPull(b, b2, y1.getPositionList().get(b.getID()),
                                                        y1.getPositionList().get(b2.getID())));
                                }
                        }
                        // Vector3dInterface force = VectorTools.sumAll(forces);
                        // System.out.println(b.getName());
                        // System.out.println(force.toString());
                        // System.out.println(VectorTools.getUnitVector(force).toString());
                        // System.out.println(VectorTools.directionVector(b.getPosition(),
                        // y1.getPositionList().get(0)));
                        rate.addVelocityChange(VectorTools.sumAll(forces).mul(1 / b.getMass()));
                }
                return rate;
        }

        public Vector3dInterface gravitationalPull(Body b1, Body b2, Vector3dInterface p1, Vector3dInterface p2) {

                // TODO: Implement physics calculations on force here
                double distance = p2.dist(p1);
                Vector3dInterface forceDirection = VectorTools.directionVector(p1, p2);
                double force = GRAV_CONSTANT * b1.getMass() * b2.getMass() / Math.pow(distance, 2);
                return forceDirection.mul(force);
        }

        public static double getAU() {
                return AU;
        }

}
