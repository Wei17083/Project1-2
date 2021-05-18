package NewtonRhapson;

public class Matrix {

    public double[][] matrix;
    public double[][] cofactorMatrix = new double[3][3];
    public double[][] adjustedMatrix = new double[3][3];
    public double[][] minorMatrix = new double[2][2];

    public Matrix (double[][] originalMatrix){
        matrix = originalMatrix;
    }

    public double[][] inverse(){
        double[][] inverse = new double[3][3];
        cofactorMatrix = cofactorMatrix();
        adjustedMatrix = adjustSign();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                print(adjustedMatrix);
                inverse[i][j] = adjustedMatrix[i][j]*1/getDeterminant();
            }
        }
        return inverse;
    }

    public double cofactor(double[][] minorMatrix){
        return minorMatrix[0][0]*minorMatrix[1][1]-minorMatrix[0][1]*minorMatrix[1][0];
    }

    public double[][] cofactorMatrix(){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                minorMatrix = getMinorMatrix(i,j);
                cofactorMatrix[i][j] = cofactor(minorMatrix);
            }
        }
        return cofactorMatrix;
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

    public double[][] adjustSign(){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(i%2 == 0){
                    if(j == 1){
                        adjustedMatrix[i][j] = -cofactorMatrix[i][j];
                    }else{
                        adjustedMatrix[i][j] = cofactorMatrix[i][j];
                    }
                } else {
                    if(j%2 == 0){
                        adjustedMatrix[i][j] = -cofactorMatrix[i][j];
                    } else {
                        adjustedMatrix[i][j] = cofactorMatrix[i][j];
                    }
                }
            }
        }
        return adjustedMatrix;
    }

    public double getDeterminant(){
        return matrix[0][0]*cofactorMatrix[0][0]-matrix[0][1]*cofactorMatrix[0][1]+matrix[0][2]*cofactorMatrix[0][2];
    }

    public static void main(String[] args){
        double[][] matrix = {{-1, -2, 2},{2, 1, 1},{3, 4, 5}};
        Matrix m = new Matrix(matrix);
        System.out.println(m.inverse()[0][1]);
    }

    public void print(double[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
