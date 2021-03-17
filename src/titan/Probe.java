package titan;

import java.util.ArrayList;

public class Probe implements ProbeSimulatorInterface{

    public SolarSystem system;
    public final double MASS = 15000;

    public Probe (SolarSystem system){
        this.system = system;
    }

    /*
     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1April 2020.
     *
     * @param   p0      the starting position of the probe, relative to the earth'sposition.
     * @param   v0      the starting velocity of the probe, relative to the earth'svelocity.
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     * @return  an array of size ts.length giving the position of the probe at eachtime stated,
     *          taken relative to the Solar System barycentre.
     */

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        double stepSize = 100;
        int size = ts.length-1;
        int iterations = (int) (Math.round(ts[size]) + 1);
        State[] stateList = (State[])system.solve(system, system.getState(), ts[size], stepSize);
        Vector3dInterface[] probePositions = new Vector3dInterface[size];
        probePositions[0] = p0;
        Vector3dInterface currentPosition = p0;
        Vector3dInterface currentVelocity = v0;
        int index = 1;
        for (int i = 1; i < iterations && index < ts.length; i++) {
            if (Math.abs((i-1)*stepSize - ts[index]) < Math.abs(i*stepSize - ts[index])) {
                probePositions[index] = currentPosition;
                index++;
            }
            ArrayList<Vector3dInterface> changes = step(currentPosition, currentVelocity, stateList[i-1], stepSize);
            probePositions[i] = changes.get(0);
            currentPosition = changes.get(0);
            currentVelocity = changes.get(1);
        }
        return probePositions;
    }

    /*
     * Simulate the solar system with steps of an equal size.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range.
     *
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre
     */
    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        State[] statesList = (State[]) system.solve(system, system.getState(), tf, h);
        int size = (int) Math.round(tf / h + 1);
        Vector3dInterface[] probePositions = new Vector3dInterface[size];
        Vector3dInterface[] probeVelocities = new Vector3dInterface[size];
        probePositions[0] = p0;
        probeVelocities[0] = v0;

        for (int i = 1; i < size; i++){
            ArrayList<Vector3dInterface> changes = step(probePositions[i-1], probeVelocities[i-1],statesList[i-1], h);
            probePositions[i] = changes.get(0);
            probeVelocities[i] = changes.get(1);
        }

        return probePositions;
    }

    public ArrayList<Vector3dInterface> step (Vector3dInterface p, Vector3dInterface v, State s, double h){
        Vector3dInterface newPosition = p.addMul(h, v);
        ArrayList<Vector3dInterface> forces = new ArrayList<>();
        for(int j = 0; j < system.getBodies().length; j++){
            forces.add(system.gravitationalPull(MASS, system.getBodies()[j].getMass(), p, s.getPositionList().get(j)));
        }
        Vector3dInterface newVelocity = v.addMul(h, VectorTools.sumAll(forces).mul(1/MASS));
        ArrayList<Vector3dInterface> step = new ArrayList<>();
        step.add(newPosition);
        step.add(newVelocity);
        return step;
    }
}
