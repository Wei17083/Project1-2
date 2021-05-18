package Verlet;

import Solvers.DifferentialEquationSolver;
import titan.*;

import java.util.ArrayList;

public class VerletSolver {

    private final double GRAV_CONSTANT = 6.674E-11;

    private final Body[] bodyList = BodyList.getBodyList();
    private final double[] MASSES = new double[bodyList.length];


    private final ArrayList<Vector3dInterface> positions = new ArrayList<>();
    private final ArrayList<Vector3dInterface> previousPositions = new ArrayList<>();
    private final ArrayList<Vector3dInterface> velocities = new ArrayList<>();
    private final Vector3dInterface[] acceleration;

    private final ArrayList<State> stateListResults = new ArrayList<>();

    private final double stepSize;
    private final double numOfSteps;

    public VerletSolver(State ogState, double timeStep, double numOfSteps){

        for(int i = 0; i < bodyList.length; i++){
            MASSES[i] = bodyList[i].getMass();
        }

        this.stateListResults.add(ogState);

        for(Vector3dInterface v : ogState.getPositionList()) {
            positions.add(v);
            previousPositions.add(v);
        }

        velocities.addAll(ogState.getVelocityList());

        acceleration = new Vector3dInterface[positions.size()];

        updateAcceleration();

        this.stepSize = timeStep;
        this.numOfSteps = numOfSteps;
    }

    public State[] doVerlet(){

        findFirstPositionWithVV();
        addNewStateToResults(1);

        for(int i = 2; i < numOfSteps; i++) {
            nextPosition();
            addNewStateToResults(i* stepSize);
        }

        return this.stateListResults.toArray(new State[0]);
    }

    public void nextPosition() {
        for(int i = 0; i < positions.size(); i++){

            Vector3dInterface pos = positions.get(i).mul(2);
            Vector3dInterface pre_pos = previousPositions.get(i).mul(-1);
            Vector3dInterface acc = acceleration[i].mul(stepSize * stepSize);

            ArrayList<Vector3dInterface> vs = new ArrayList<>();
            vs.add(pos);
            vs.add(pre_pos);
            vs.add(acc);

            previousPositions.set(i, positions.get(i));
            positions.set(i, VectorTools.sumAll(vs));
        }
        updateAcceleration();
    }

    public void findFirstPositionWithVV(){

        // update position
        for(int i = 0; i < positions.size(); i++){

            Vector3dInterface pos = positions.get(i);
            Vector3dInterface vel = velocities.get(i).mul(stepSize);
            Vector3dInterface acc = acceleration[i].mul(stepSize * stepSize *(0.5));

            ArrayList<Vector3dInterface> vs = new ArrayList<>();
            vs.add(pos);
            vs.add(vel);
            vs.add(acc);

            positions.set(i, VectorTools.sumAll(vs));
        }

        updateAcceleration();
    }

    public void updateAcceleration(){

        for(int i = 0; i < positions.size(); i++){

            Vector3dInterface body1 = positions.get(i);
            double mass1 = MASSES[i];

            ArrayList<Vector3dInterface> forces = new ArrayList<>();

            for(int j = 0; j < positions.size(); j++){
                if(j != i){
                    Vector3dInterface body2 = positions.get(j);
                    double mass2 = MASSES[j];
                    forces.add(gravitationalPull(mass1, mass2, body1, body2));
                }
            }
            acceleration[i] = VectorTools.sumAll(forces).mul(1/mass1);
        }
    }

    private Vector3dInterface gravitationalPull(double mass1, double mass2, Vector3dInterface p1, Vector3dInterface p2) {
        double distance = p2.dist(p1);
        Vector3dInterface forceDirection = VectorTools.directionVector(p1, p2);
        double force = GRAV_CONSTANT * mass1 * mass2 / Math.pow(distance, 2);
        return forceDirection.mul(force);
    }

    private void addNewStateToResults(double time){ stateListResults.add(new State(time, new ArrayList<>(this.positions), this.velocities)); }
}