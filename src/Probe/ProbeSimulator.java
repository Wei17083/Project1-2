package Probe;

import titan.Body;
import titan.BodyList;
import titan.State;
import titan.Vector3dInterface;
import System.SolarSystem;

import java.util.ArrayList;

public class ProbeSimulator implements ProbeSimulatorInterface{
    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        return new Vector3dInterface[0];
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        Body[] bodyList = BodyList.getBodyList();
        setRelativeProbeParameters(bodyList, p0, v0);
        SolarSystem system = new SolarSystem(BodyList.getBodyList(), p0, v0);

        int id = 11;
        for (Body body: bodyList) {
            if(body.getName().equals("Probe")) {
                id = body.getID();
            }
        }
        return system.trajectoryByID(id, tf, h);
    }

    public void setRelativeProbeParameters(Body[] bodyList, Vector3dInterface p0, Vector3dInterface v0){
        Body earth = bodyList[3];
        Body probe = bodyList[11];
        for (Body body: bodyList) {
            if(body.getName().equals("Earth")){
                earth = body;
            }
            if(body.getName().equals("Probe")){
                probe = body;
            }
        }
        probe.setPosition(earth.getPosition().add(p0));
        probe.setVelocity(earth.getVelocity().add(v0));
    }


}
