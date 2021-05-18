package Probe;

import titan.Body;
import titan.BodyList;
import titan.State;
import titan.Vector3dInterface;
import System.SolarSystem;

import java.util.ArrayList;
import java.util.Vector;

public class ProbeSimulator implements ProbeSimulatorInterface{
    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        return new Vector3dInterface[0];
    }


    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        Body[] bodyList = BodyList.getBodyList();
        setRelativeProbeParameters(bodyList, p0, v0);
        return actualTrajectory(tf, h);
    }

    public Vector3dInterface[] actualTrajectory(double tf, double h){
        SolarSystem system = new SolarSystem(BodyList.getBodyList());
        int id = 11;
        return system.trajectoryByID(id, tf, h);
    }

    public Vector3dInterface[] trajectoryAbsoluteInitialVelocity(Vector3dInterface p0, Vector3dInterface v0, double tf, double h){
        setProbeParameters(p0, v0);
        return actualTrajectory(tf, h);
    }

    public void setRelativeProbeParameters(Body[] bodyList, Vector3dInterface p0, Vector3dInterface v0){
        Body earth = bodyList[3];

        for (Body body: bodyList) {
            if(body.getName().equals("Earth")){
                earth = body;
            }

        }
        setProbeParameters(earth.getPosition().add(p0), earth.getVelocity().add(v0));
    }

    public void setProbeParameters(Vector3dInterface p0, Vector3dInterface v0){
        Body[] bodyList = BodyList.getBodyList();
        Body probe = bodyList[11];
        for (Body body: bodyList) {
            if(body.getName().equals("Probe")){
                probe = body;
            }
        }
        probe.setPosition(p0);
        probe.setVelocity(v0);
    }


}
