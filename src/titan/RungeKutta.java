package titan;

public class RungeKutta {
    
    public double dydx(double x, double y) { 
        return ((x-y)/2); // y′=F(x,y), this return is an example
    }

    // finds value of y for a given x using step size h and initial value y0 at x0
    public double rungeKutta(double x0, double y0, double x, double h)
    {

        // count number of iterations
        int n = (int)((x - x0) / h);
  
        double k1, k2, k3, k4;
        double y = y0;

        for (int i = 1; i <= n; i++) {
            // apply Runge Kutta Formula
            k1 = h * (dydx(x0, y));
            k2 = h * (dydx(x0 + 0.5*h, y + 0.5*k1));
            k3 = h * (dydx(x0 + 0.5*h, y + 0.5*k2));
            k4 = h * (dydx(x0 + h, y + k3));
  
            // Update next value of y
            y = y + (1.0 / 6.0) * (k1 + 2*k2 + 2*k3 + k4);
              
            // Update next value of x
            x0 = x0 + h;
        }
        return y;
    }
}
