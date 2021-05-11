package Solvers.derivatives;

import titan.Vector3dInterface;

import java.util.ArrayList;

public interface Derivative {
    public Vector3dInterface getDerivative(int ID);
    public ArrayList<Vector3dInterface> getAllDerivatives();
}
