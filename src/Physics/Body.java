package Physics;

import titan.*;
import java.awt.*;

/**
 *
 */
public class Body {
    private String name;
    private double mass;
    private Vector position;
    private Vector velocity;
    private final double radius;
    private Color color; // color to visualise

    /**
     * This method draws the body using its position, radius and color
     */
    public void draw() {
        position.drawBody(radius, color);
    }

    /**
     * returns the mass of the object
     * 
     * @return mass of the object
     */
    public double getMass() {
        return mass;
    }

    /**
     * returns the velocity of the object
     * 
     * @return velocity vector of the object
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * returns the position of the object
     * 
     * @return position vector of the object
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * returns the radius of the body
     *
     * @return radius of the body
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Constructor of the body class
     *
     * @param name     sets the name of the object
     * @param mass     sets the mass of the object
     * @param position sets the position vector of the object
     * @param velocity sets the velocity vector of the object
     * @param radius   sets the radius of the object
     * @param c        sets the Color of the object, if NULL will be set to black
     */
    public Body(String name, double mass, Vector position, Vector velocity, double radius, Color c) {
        this.name = name;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        if (c == null) // if there is no color input, default to black
            color = Color.BLACK;
        else
            color = c;
    }

    /**
     * Changes the mass of the body
     * 
     * @param mass set the mass of the body to this value
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Changes the position vector of the body
     * 
     * @param position set the position vector of the body to this value
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /**
     * changes the velocity vector of the body
     * 
     * @param velocity set the velocity
     */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
