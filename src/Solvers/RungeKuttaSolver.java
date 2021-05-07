package Solvers;

import Solvers.derivatives.Derivative;
import titan.Body;
import titan.ChangeRate;
import titan.State;

public class RungeKuttaSolver extends DifferentialEquationSolver{

    /** Calculates one step of Runge Kutta 4th order given an initial value, the differential equation and a stepsize
     *
     * @param currentState      current state of the system of the equation
     * @param bodyList          List of bodies required to calculate the accelerations
     * @param h                 stepsize
     * @return                  solution for the equation at t = t0+h
     */
    @Override
    public State stateStep(State currentState, Body[] bodyList, double h) {
        //make Derivative for positions and velocities
        Derivative dfPositions = createPositionDerivative(currentState);
        Derivative dfVelocities = createVelocityDerivative(currentState, bodyList);


        //TODO: use the getAllDerivatives to calculates the "k's"
        ChangeRate k1 = new ChangeRate(dfPositions.getAllDerivatives(), dfVelocities.getAllDerivatives()).mul(h);
        State s1 = currentState.addMul(0.5, k1);
        double coefficientK1 = 1.0/0.6;
        ChangeRate k2 = new ChangeRate(createPositionDerivative(s1).getAllDerivatives(), createVelocityDerivative(s1, bodyList).getAllDerivatives());
        State s2 = currentState.addMul(0.5, k2);
        double coefficientK2 = 2.0/0.6;
        ChangeRate k3 = new ChangeRate(createPositionDerivative(s2).getAllDerivatives(), createVelocityDerivative(s2, bodyList).getAllDerivatives());
        State s3 = currentState.addMul(1, k3);
        double coefficientK3 = 2.0/0.6;
        ChangeRate k4 = new ChangeRate(createPositionDerivative(s3).getAllDerivatives(), createVelocityDerivative(s3, bodyList).getAllDerivatives());
        double coefficientK4 = 1.0/0.6;

        State returnState = currentState.addMul(coefficientK1, k1).addMul(coefficientK2,k2).addMul(coefficientK3,k3).addMul(coefficientK4,k4);
        returnState.setStateTime(currentState.getStateTime() + h);

        return returnState;
    }
}
