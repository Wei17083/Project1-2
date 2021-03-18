package titan;

import BruteForce.BruteForce;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.Tool;

public class testPhysics {
        public static void main(String[] args) throws InterruptedException, IOException {
                Body sun = new Body("Sun", 0, 1.988500e30,
                                new Vector(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06),
                                new Vector(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01),
                                6.95508e8, new Color(249, 249, 127)); // yellow+white

                Body mercury = new Body("Mercury", 1, 3.302e23,
                                new Vector(6.047855986424127e+06, -6.801800047868888e+10, -5.702742359714534e+09),
                                new Vector(3.892585189044652e+04, 2.978342247012996e+03, -3.327964151414740e+03),
                                2.4397e6, new Color(145, 162, 171)); // grey

                Body venus = new Body("Venus", 2, 4.8685e24,
                                new Vector(-9.435345478592035e+10, 5.350359551033670e+10, 6.131453014410347e+09),
                                new Vector(-1.726404287724406e+04, -3.073432518238123e+04, 5.741783385280979e-04),
                                6.0518e6, new Color(181, 92, 69)); // red+brown+grey

                Body earth = new Body("Earth", 3, 5.97219e24,
                                new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06),
                                new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01),
                                6.371e6, new Color(114, 213, 190)); // green+blue+grey

                Body moon = new Body("Moon", 4, 7.349e22,
                                new Vector(-1.472343904597218e+11, -2.822578361503422e+10, 1.052790970065631e+07),
                                new Vector(4.433121605215677e+03, -2.948453614110320e+04, 8.896598225322805e+01),
                                1.7375e6, Color.gray);

                Body mars = new Body("Mars", 5, 6.4171e23,
                                new Vector(-3.615638921529161e+10, -2.167633037046744e+11, -3.687670305939779e+09),
                                new Vector(2.481551975121696e+04, -1.816368005464070e+03, -6.467321619018108e+02),
                                3.3895e6, new Color(171, 62, 41)); // red+brown+dark

                Body jupiter = new Body("Jupiter", 6, 1.89813e27,
                                new Vector(1.781303138592153e+11, -7.551118436250277e+11, -8.532838524802327e+08),
                                new Vector(1.255852555185220e+04, 3.622680192790968e+03, -2.958620380112444e+02),
                                6.9911e7, new Color(215, 148, 61)); // orange+brown

                Body saturn = new Body("Saturn", 7, 5.6834e26,
                                new Vector(6.328646641500651e+11, -1.358172804527507e+12, -1.578520137930810e+09),
                                new Vector(8.220842186554890e+03, 4.052137378979608e+03, -3.976224719266916e+02),
                                5.8232e7, new Color(208, 180, 40)); // gold+brown

                Body titan = new Body("Titan", 8, 1.34553e23,
                                new Vector(6.332873118527889e+11, -1.357175556995868e+12, -2.134637041453660e+09),
                                new Vector(3.056877965721629e+03, 6.125612956428791e+03, -9.523587380845593e+02),
                                2.5755e6, new Color(163, 191, 89)); // yellow+green

                Body uranus = new Body("Uranus", 9, 8.6813e25,
                                new Vector(2.395195786685187e+12, 1.744450959214586e+12, -2.455116324031639e+10),
                                new Vector(-4.059468635313243e+03, 5.187467354884825e+03, 7.182516236837899e+01),
                                2.5362e7, new Color(121, 227, 235)); // blue+green

                Body neptune = new Body("Neptune", 10, 1.02413e26,
                                new Vector(4.382692942729203e+12, -9.093501655486243e+11, -8.227728929479486e+10),
                                new Vector(1.068410720964204e+03, 5.354959501569486e+03, -1.343918199987533e+02),
                                2.4622e7, Color.blue);
                Body probe = new Body("Probe", 11, 15000, new Vector(0, 0, 0), new Vector(0, 0, 0), 100, null);

                // create arrays of bodies and corresponding forces
                Body[] bodies = new Body[] { sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, uranus,
                                neptune, probe };

                ToolsCSV csv = new ToolsCSV(null, bodies.length);
                GUI.visualise(bodies, ToolsCSV.getAllPositions(), ToolsCSV.getProbeTrajectory());

                // SolarSystem system = new SolarSystem(bodies);

                // // System.out.println(system.getState().toString());
                // State state = (State) system.step(system, 86400, system.getState(), 86400);
                // // System.out.println(state.toString());

                // double[] ts = { 0, 31556926 }; // 31556926
                // StateInterface[] stateList = system.solve(system, system.getState(), ts);
                // // System.out.println(stateList[1].toString());

                // double tf = 31556926;
                // // System.out.println("tf = "+tf);
                // StateInterface[] stateList2 = system.solve(system, system.getState(), tf,
                // 1000);
                // State[] stateList3 = new State[stateList2.length];
                // for (int i = 0; i < stateList2.length; i++) {
                // stateList3[i] = (State) stateList2[i];
                // }
                // // System.out.println(stateList2[stateList2.length-1].toString());

                // ArrayList<Vector3dInterface> earthPositionList = new ArrayList<>();
                // for (State s : stateList3) {
                // Vector3dInterface v = s.getPositionList().get(3);
                // earthPositionList.add(s.getPositionList().get(3));
                // }
                // // System.out.println(earthPositionList.get(0).getX()+",
                // // "+earthPositionList.get(0).getY()+", "+earthPositionList.get(0).getZ());
                // //
                // System.out.println(earthPositionList.get(earthPositionList.size()-1).getX()+",
                // // "+earthPositionList.get(earthPositionList.size()-1).getY()+",
                // // "+earthPositionList.get(earthPositionList.size()-1).getZ());
                // //
                // System.out.println(earthPositionList.get((earthPositionList.size()-1)/2).getX()+",
                // // "+earthPositionList.get((earthPositionList.size()-1)/2).getY()+",
                // // "+earthPositionList.get((earthPositionList.size()-1)/2).getZ());

                // // System.out.println(new Vector(27771.89015815458, -40238.56416078634,
                // // -769.065384128892).norm());

                // double stepSize = 1000;
                // StateInterface[] states = system.solve(system, system.getState(), 31556926,
                // stepSize);

                // Vector3dInterface velocity = new Vector(27771.89015815458,
                // -40238.56416078634, -769.065384128892);

                // Vector3dInterface unitVector = VectorTools.getUnitVector(velocity);
                // Vector3dInterface EarthP = new Vector(-1.471922101663588e+11,
                // -2.860995816266412e+10,
                // 8.278183193596080e+06);
                // Vector3dInterface EarthV = new Vector(5.427193405797901e+03,
                // -2.931056622265021e+04,
                // 6.575428158157592e-01);
                // double EarthR = 6.371e6;

                // Vector3dInterface position = EarthP.addMul(EarthR, unitVector);
                // Vector3dInterface velocityTotal = EarthV.add(velocity);

                // Probe spaceship = new Probe(system, states);
                // Vector3dInterface[] trajectory = spaceship.trajectory(position,
                // velocityTotal, 31556926, stepSize);
                // System.out.println("Minimum distance: " + BruteForce.getMinimum(trajectory,
                // states, 8));
                // System.out.println("Minimum distance Jupiter: " +
                // BruteForce.getMinimum(trajectory, states, 6));

                // ArrayList<StateInterface> arListPositions = new
                // ArrayList<>(Arrays.asList(stateList2));
                // csv.createCSV();
                // ToolsCSV.createProbeCSV(trajectory);
                // BruteForce.bruteforce(system);

        }
}