package titan;

import java.awt.Color;
import java.lang.Math;

public class Vector implements Vector3dInterface {
    private static final boolean DEBUG = true;
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public Vector3dInterface add(Vector3dInterface other) {
        Vector3dInterface v = new Vector();
        v.setX(x + other.getX());
        v.setY(y + other.getY());
        v.setZ(z + other.getZ());
        return v;
    }

    @Override
    public Vector3dInterface sub(Vector3dInterface other) {
        Vector3dInterface v = new Vector();
        v.setX(x - other.getX());
        v.setY(y - other.getY());
        v.setZ(z - other.getZ());
        return v;
    }

    @Override
    public Vector3dInterface mul(double scalar) {
        Vector3dInterface v = new Vector();
        v.setX(x * scalar);
        v.setY(y * scalar);
        v.setZ(z * scalar);
        return v;
    }

    @Override
    public Vector3dInterface addMul(double scalar, Vector3dInterface other) {
        Vector3dInterface v = new Vector();
        v.setX(x + other.getX() * scalar);
        v.setY(y + other.getY() * scalar);
        v.setZ(z + other.getZ() * scalar);
        return v;
    }

    @Override
    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    @Override
    public double dist(Vector3dInterface other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2) + Math.pow(z - other.getZ(), 2));
    }



    /**
     * This method draws a circle on this vectors position with the given parameters
     * 
     * @param radius radius of the circle
     * @param color  color of the circle
     */
    public void drawBody(double radius, Color color) {
        StdDraw.setPenColor(color);
        // using real scale of planets wouldnt give us a good overview
        // one can play around with the radius so that it looks presentable
        StdDraw.filledCircle(this.x, this.y, 5e9 * Math.log10(radius));
        // line is just to help find offscreen planets
        StdDraw.line(this.x, this.y, 0, 0);
    }
}
