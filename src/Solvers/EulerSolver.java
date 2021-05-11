package Solvers;

import Solvers.DifferentialEquationSolver;
import Solvers.derivatives.Derivative;
import titan.Body;
import titan.ChangeRate;
import titan.State;

public class EulerSolver extends DifferentialEquationSolver {

    public State stateStep(State currentState, Body[] bodyList, double h) {
        Derivative dfPositions = createPositionDerivative(currentState);
        Derivative dfVelocities = createVelocityDerivative(currentState, bodyList);
        ChangeRate rate = new ChangeRate(dfPositions.getAllDerivatives(), dfVelocities.getAllDerivatives());
        return currentState.addMul(h, rate);
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
