package Verlet;

import titan.State;
import titan.Vector3dInterface;
import titan.VectorTools;

import java.util.ArrayList;

public class VerletSolver {

    private final double[] MASSES = new double[]{1.988500e30, 3.302e23, 4.8685e24, 5.97219e24, 7.349e22, 6.4171e23, 1.89813e27, 5.6834e26, 1.34553e23, 8.6813e25, 1.02413e26};
    private final double GRAV_CONSTANT = 6.674E-11;
    private final ArrayList<Vector3dInterface> positions = new ArrayList<>();
    private final ArrayList<Vector3dInterface> previousPositions = new ArrayList<>();
    private final ArrayList<Vector3dInterface> velocities = new ArrayList<>();
    private final Vector3dInterface[] acceleration;

    private final ArrayList<State> states = new ArrayList<>();

    private final double timeStep;
    private final double numOfSteps;

    public VerletSolver(State ogState, double timeStep, double numOfSteps){

        this.states.add(ogState);

        for(Vector3dInterface v : ogState.getPositionList()) {
            positions.add(v);
            previousPositions.add(v);
        }

        velocities.addAll(ogState.getVelocityList());

        acceleration = new Vector3dInterface[positions.size()];

        updateAcceleration();

        this.timeStep = timeStep;
        this.numOfSteps = numOfSteps;
    }

    public ArrayList<State> doVerlet(){

        nextPositionVV();
        addNewState(1);

        for(int i = 2; i < numOfSteps; i++) {
            nextPosition();
            addNewState(i*timeStep);
        }
        return this.states;
    }

    public void nextPosition() {

        for(int i = 0; i < positions.size(); i++){

            Vector3dInterface pos = positions.get(i).mul(2);
            Vector3dInterface pre_pos = previousPositions.get(i).mul(-1);
            Vector3dInterface acc = acceleration[i].mul(timeStep*timeStep);

            ArrayList<Vector3dInterface> vs = new ArrayList<>();
            vs.add(pos);
            vs.add(pre_pos);
            vs.add(acc);

            previousPositions.set(i, positions.get(i));
            positions.set(i, VectorTools.sumAll(vs));
        }

        updateAcceleration();

    }

    public void nextPositionVV(){

        // update position
        for(int i = 0; i < positions.size(); i++){
            Vector3dInterface pos = positions.get(i);
            Vector3dInterface vel = velocities.get(i).mul(timeStep);
            Vector3dInterface acc = acceleration[i].mul(timeStep*timeStep*(0.5));

            ArrayList<Vector3dInterface> vs = new ArrayList<>();
            vs.add(pos);
            vs.add(vel);
            vs.add(acc);

            positions.set(i, VectorTools.sumAll(vs));

        }

        //update acceleration
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

    private void addNewState(double t){
        states.add(new State(t, this.positions, this.velocities));
    }
}