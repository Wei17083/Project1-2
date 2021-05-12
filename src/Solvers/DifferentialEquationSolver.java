package Solvers;

import Solvers.derivatives.*;
import titan.*;

import java.util.ArrayList;

/**Contains solvers that will solve one step of a differential equation
 *
 */
public abstract class DifferentialEquationSolver {

    /**
     *
     * @param bodies        List of bodies in the system
     * @param initialState  State of the system at t0
     * @param finalTime     Time up to which to evaluate the solar system
     * @param stepSize      stepsize with which to solve the differential equation
     * @return              List of the states at each step from the start till finalTime
     */
    public State[] solve(Body[] bodies, State initialState, double finalTime, double stepSize ){
        int steps = (int) Math.ceil(finalTime/stepSize);    // final step will be done outside the loop since it's size can vary
        int size = steps+1; // the final array holds all the steps and the initial state
        State[] stateList = new State[size];
        stateList[0] = initialState;

        for (int i = 1; i < steps +1 ; i++) {
            if(i == steps) {  //final step will have varying stepsize
                stepSize = finalTime - (i-1)*stepSize;
            }
            stateList[i] = this.stateStep(stateList[i-1], bodies, stepSize);
        }
        return stateList;
    }

    public abstract State stateStep(State currentState, Body[] bodyList, double h);


    protected Derivative createPositionDerivative(State state) {
        return new DerivativePosition(state.getVelocityList());
    }

    protected Derivative createVelocityDerivative(State state, Body[] bodyList) {
        return new DerivativeVelocity(state.getPositionList(), bodyList);
    }
}
