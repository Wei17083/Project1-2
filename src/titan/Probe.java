//package titan;
//
//import java.util.ArrayList;
//
//public class Probe  extends Body implements ProbeSimulatorInterface{
//
//    public SolarSystem system;
//
//    public Probe (SolarSystem system){
//        this.system = system;
//    }
//
//    /*
//     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1April 2020.
//     *
//     * @param   p0      the starting position of the probe, relative to the earth'sposition.
//     * @param   v0      the starting velocity of the probe, relative to the earth'svelocity.
//     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
//     * @return  an array of size ts.length giving the position of the probe at eachtime stated,
//     *          taken relative to the Solar System barycentre.
//     */
//
//    @Override
//    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
//
//    }
//
//    /*
//     * Simulate the solar system with steps of an equal size.
//     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range.
//     *
//     * @param   tf      the final time of the evolution.
//     * @param   h       the size of step to be taken
//     * @return  an array of size round(tf/h)+1 giving the position of the probe at each time stated,
//     *          taken relative to the Solar System barycentre
//     */
//    @Override
//    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
//        StateInterface[] statesPlanets = system.solve(system, system.getState(), tf, h);
//        ArrayList<Vector3dInterface> probePositions = new ArrayList<>();
//        ArrayList<Vector3dInterface> probeVelocities = new ArrayList<>();
//        ArrayList<Vector3dInterface> probeAccelerations = new ArrayList<>();
//        probePositions.add(p0);
//        probeVelocities.add(v0);
//        int size = (int) Math.round(tf / h + 1);
//        for (int i = 0; i < size; i++){
//            ArrayList<Vector3dInterface> forces = new ArrayList<>();
//            State y = (State) statesPlanets[i];
//            for(int j = 0; j < system.getBodies().length; j++){
//                forces.add(system.gravitationalPull(this, system.getBodies()[j],this.getPosition(), y.getPositionList().get(j)));
//            }
//            Vector3dInterface netForce = VectorTools.sumAll(forces);
//            probeAccelerations.add(VectorTools.sumAll(forces).mul(1 / this.getMass()));
//            probeVelocities.add(probeVelocities.get(i).addMul(h, probeAccelerations.get(i)));
//        }
//
//        for (int i = 0; i < size; i++){
//            probePositions.add(probePositions.get(i).addMul(h, probeVelocities.get(i)));
//        }
//
//        return (Vector3dInterface[]) probePositions.toArray();
//    }
//}
