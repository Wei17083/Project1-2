package Solvers;

public class SolverToUse {

    public static String solver = "euler";
    public SolverToUse(){}
    public void setSolver(String solver){
        this.solver = solver;
    }

    public static String getSolver() {
        return solver;
    }
}
