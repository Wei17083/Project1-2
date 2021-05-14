package System;

import Solvers.DifferentialEquationSolver;
import Solvers.EulerSolver;
import Solvers.RungeKuttaSolver;
import titan.*;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SolarSystem {

        public final String SOLVER = "euler";

        public final double GRAV_CONSTANT = 6.674E-11;
        // One AU is approximately the average distance between the Earth and the Sun
        // value taken from https://cneos.jpl.nasa.gov/glossary/au.html
        public static final double AU = 1.495978707e11;

        private final Body[] bodies;
        private State initialState;
        private final String SOLVERS = "euler";

        public SolarSystem(Body[] bodies) {
                this.bodies = bodies;
        }

        public SolarSystem(Body[] bodies, Vector3dInterface probePosition, Vector3dInterface probeVelocity) {
                this.bodies = bodies;
        }

        public State[] calculateTrajectories(double finalTime, double stepSize){
                State initialState = createInitialState(bodies);
                DifferentialEquationSolver solver;
                if(SOLVERS.equals("euler"))  solver = new EulerSolver();
                else  solver = new RungeKuttaSolver();

                return solver.solve(bodies, initialState, finalTime, stepSize);

        }

        public Vector3dInterface[] trajectoryByID(int id, double finalTime, double stepSize) {
                State[] stateList = calculateTrajectories(finalTime, stepSize);
                ArrayList<Vector3dInterface> trajectoryList = new ArrayList<>();
                ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
                for (State state: stateList) {
                        trajectoryList.add(state.getPositionList().get(id));
                        velocityList.add((state.getVelocityList().get(id)));
                }
                Vector3dInterface[] trajectory = new Vector3dInterface[stateList.length];
                Vector3dInterface[] velocities = new Vector3dInterface[stateList.length];
                velocities = velocityList.toArray(velocities);
                System.out.println(Arrays.toString(velocities));
                return trajectoryList.toArray(trajectory);
        }

        private State createInitialState(Body[] bodies){

                ArrayList<Vector3dInterface> positionList = new ArrayList<>();
                ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
                for (Body b : bodies) {
                        positionList.add(b.getPosition());
                        velocityList.add(b.getVelocity());
                }
                for(Body b : bodies){
                        assert (VectorTools.equals(b.getPosition(),positionList.get(b.getID())));
                        assert (VectorTools.equals(b.getVelocity(),velocityList.get(b.getID())));
                }
                return new State(0, positionList, velocityList);
        }



        public Body[] getBodies() {
                return bodies;
        }

        public State getState() {
                return initialState;
        }

        public static double getAU() {
                return AU;
        }

}
