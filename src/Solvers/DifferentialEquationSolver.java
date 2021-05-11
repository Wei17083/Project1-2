package Solvers;

import Solvers.derivatives.*;
import titan.*;

import java.util.ArrayList;

/**Contains solvers that will solve one step of a differential equation
 *
 */
public abstract class DifferentialEquationSolver {


    public abstract State stateStep(State currentState, Body[] bodyList, double h);


    protected Derivative createPositionDerivative(State state) {
        return new DerivativePosition(state.getVelocityList());
    }

    protected Derivative createVelocityDerivative(State state, Body[] bodyList) {
        return new DerivativeVelocity(state.getPositionList(), bodyList);
    }
}
