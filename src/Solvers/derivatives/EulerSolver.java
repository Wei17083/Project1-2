package Solvers.derivatives;

import Solvers.DifferentialEquationSolver;
import titan.*;

import java.util.ArrayList;

public class EulerSolver extends DifferentialEquationSolver {

    public State stateStep(State currentState, Body[] bodyList, double h) {
        Derivative dfPositions = createPositionDerivative(currentState);
        Derivative dfVelocities = createVelocityDerivative(currentState, bodyList);
        ChangeRate rate = new ChangeRate(dfPositions.getAllDerivatives(), dfVelocities.getAllDerivatives());
        return currentState.addMul(h, rate);
    }

    public ArrayList<Vector3dInterface> trajectoryStep (Vector3dInterface p, Vector3dInterface v, State s, double h, Body[] bodyList){
        DerivativePosition dfPosition = new DerivativePosition(v);
        DerivativeVelocity dfVelocity = new DerivativeVelocity(s.getPositionList(), p, bodyList);
        ArrayList<Vector3dInterface> stepTrajectory = new ArrayList<>();
        stepTrajectory.add(p.addMul(h, dfPosition.getDerivative()));
        stepTrajectory.add(v.addMul(h, dfVelocity.getDerivative()));
        return stepTrajectory;
    }

    public Vector3dInterface[] trajectoryFinal(SolarSystem system, Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        StateInterface[] statesList = system.solve(system, system.getState(), tf, h);
        int size = (int) Math.round(tf / h + 1);
        Vector3dInterface[] probePositions = new Vector3dInterface[size];
        Vector3dInterface[] probeVelocities = new Vector3dInterface[size];
        probePositions[0] = p0;
        probeVelocities[0] = v0;

        for (int i = 1; i < size; i++){
            ArrayList<Vector3dInterface> changes = trajectoryStep(probePositions[i-1], probeVelocities[i-1],(State)statesList[i-1], h, system.getBodies());
            probePositions[i] = changes.get(0);
            probeVelocities[i] = changes.get(1);
        }

        return probePositions;
    }
//    public State eulerStepState(State currentState, Derivative dfPositions, Derivative dfVelocities, double h) {
//        ChangeRate rate = new ChangeRate(dfPositions.getAllDerivatives(), dfVelocities.getAllDerivatives());
//        return currentState.addMul(h, rate);

//        ArrayList<Vector3dInterface> newPositions = eulerStepList(currentState.getPositionList(), dfPositions, h);
//        ArrayList<Vector3dInterface> newVelocities = eulerStepList(currentState.getVelocityList(), dfPositions, h);
//
//        return new State(currentState.getStateTime()+h, newPositions, newVelocities);
//    }

//    public ArrayList<Vector3dInterface> eulerStepList(ArrayList<Vector3dInterface> vectorList, Derivative df, double h) {
//        ArrayList<Vector3dInterface> newVectors = new ArrayList<>();
//        for (int i = 0; i < vectorList.size(); i++) {
//            newVectors.add(eulerStep(vectorList.get(i),i, df, h));
//        }
//        return newVectors;
//    }
//
//    public Vector3dInterface eulerStep(Vector3dInterface v, int ID, Derivative df, double h) {
//        return v.addMul(h, df.getDerivative(ID));
//    }
}
