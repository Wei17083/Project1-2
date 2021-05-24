package NewtonRhapson;

import titan.Vector;
import titan.Vector3dInterface;

public class Matrix {

    public double[][] matrix;
    public double[][] cofactorMatrix = new double[3][3];
    public double[][] signMatrix = new double[3][3];
    public double[][] minorMatrix = new double[2][2];
    public double[][] adjointMatrix = new double[3][3];

    public Matrix (double[][] originalMatrix){
        matrix = originalMatrix;
    }

    public Matrix inverse(){
        double[][] inverse = new double[3][3];
        cofactorMatrix();
        adjustSign();
        adjointMatrix();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                inverse[i][j] = (adjointMatrix[i][j])*(1/getDeterminant());
            }
        }
        return new Matrix(inverse);
    }

    public double cofactor(double[][] minorMatrix){
        return minorMatrix[0][0]*minorMatrix[1][1]-minorMatrix[0][1]*minorMatrix[1][0];
    }

    public void cofactorMatrix(){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                minorMatrix = getMinorMatrix(i,j);
                cofactorMatrix[i][j] = cofactor(minorMatrix);
            }
        }
    }

    public double[][] getMinorMatrix(int row, int column){
        int k = 0;
        int z = 0;
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != row && j != column) {
                    minorMatrix[k][z] = matrix[i][j];
                    z++;
                }
            }
            z = 0;
            if(i != row) {
                k++;
            }
        }
        return minorMatrix;
    }

    public void adjustSign(){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(i%2 == 0){
                    if(j == 1){
                        signMatrix[i][j] = -cofactorMatrix[i][j];
                    }else{
                        signMatrix[i][j] = cofactorMatrix[i][j];
                    }
                } else {
                    if(j%2 == 0){
                        signMatrix[i][j] = -cofactorMatrix[i][j];
                    } else {
                        signMatrix[i][j] = cofactorMatrix[i][j];
                    }
                }
            }
        }
    }

    public double getDeterminant(){
        return matrix[0][0]*cofactorMatrix[0][0]-matrix[0][1]*cofactorMatrix[0][1]+matrix[0][2]*cofactorMatrix[0][2];
    }

    public void adjointMatrix(){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0;j < matrix.length; j++){
                if(i == j){
                    adjointMatrix[i][j] = signMatrix[i][j];
                } else {
                    adjointMatrix[i][j] = signMatrix[j][i];
                }
            }
        }
    }

    public Vector3dInterface multiplicationMatrixVector(Vector3dInterface v){
        Vector3dInterface result = new Vector(0,0,0);
        double[] resultArray = new double[3];
        double[] vector = {v.getX(), v.getY(), v.getZ()};
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                resultArray[i] = resultArray[i] + matrix[i][j]*vector[j];
            }
        }
        result.setX(resultArray[0]);
        result.setY(resultArray[1]);
        result.setZ(resultArray[2]);
        return result;
    }



}
