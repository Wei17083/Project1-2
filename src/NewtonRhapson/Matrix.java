package NewtonRhapson;

import titan.*;

public class Matrix {

    double[][] matrix = new double[3][3];

    public Vector3dInterface mul(Vector3dInterface vector){

        double newX, newY, newZ;

        newX = matrix[0][0]*vector.getX() + matrix[0][1]*vector.getY() + matrix[0][2]*vector.getZ();
        newY = matrix[1][0]*vector.getX() + matrix[1][1]*vector.getY() + matrix[1][2]*vector.getZ();
        newZ = matrix[2][0]*vector.getX() + matrix[2][1]* vector.getZ() + matrix[2][2]*vector.getZ();

        return new Vector(newX, newY, newZ);

    }
}
