package titan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToolsCSV {
    private static ArrayList<StateInterface> listStates;
    private static String fileName = ".csv";
    private static String trajectoryFile = ".csv";
    private static int n_bodies;

    public ToolsCSV(ArrayList<StateInterface> listStates, int n_bodies, String name, String trajectoryName) {
        this.listStates = listStates;
        this.n_bodies = n_bodies;
        fileName = name + fileName;
        trajectoryFile = trajectoryName + trajectoryFile;
    }

    public static void createCSV() throws FileNotFoundException {
        File csvFile = new File(fileName);
        PrintWriter out = new PrintWriter(csvFile);

        out.println("time, ID, X-Position, Y-Position, Z-Position, X-Velocity, Y-Velocity, Z-Velocity");

        for (StateInterface si : listStates) {
            State s = (State) si;
            for (int i = 0; i < s.positionList.size(); i++) {
                out.print(s.stateTime + ", "); // 1st column: time
                out.print(i + ", "); // 2nd column: ID
                out.print(s.positionList.get(i).getX() + ", "); // 3rd column: X-Position
                out.print(s.positionList.get(i).getY() + ", "); // 4th column: Y-Position
                out.print(s.positionList.get(i).getZ() + ", "); // 5th column: Z-Position
                out.print(s.velocityList.get(i).getX() + ", "); // 6th column: X-Velocity
                out.print(s.velocityList.get(i).getY() + ", "); // 7th column: Y-Velocity
                out.println(s.velocityList.get(i).getZ() + ", "); //// 8th column: Z-Velocity
            }
        }
        out.close();
    }

    public static void createProbeCSV(Vector3dInterface[] trajectory, double finalPos) throws FileNotFoundException {
        File csvFile = new File(trajectoryFile);
        PrintWriter out = new PrintWriter(csvFile);

        out.println("X-Position, Y-Position, Z-Position");// , X-Velocity, Y-Velocity, Z-Velocity");
        out.println(finalPos + ",");
        for (int i = 0; i < trajectory.length; i++) {
            out.println(VectorTools.csvCoordinates(trajectory[i]) + ",");
        }
        out.close();

    }

    // method to return the list of position vectors of a given Body (ID)
    public static List<Vector3dInterface> getCSVPositions(double ID) throws FileNotFoundException {
        List<Vector3dInterface> list = new ArrayList<Vector3dInterface>();
        FileReader f = new FileReader(fileName);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader, 1);

        while (Double.parseDouble((Reader.next())) != ID) {
            skipLine(Reader, 1);
            skipColumn(Reader, 1);
        }

        while (Reader.hasNextLine()) {

            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x, y, z));

            skipLine(Reader, n_bodies);
            skipColumn(Reader, 2);
        }
        Reader.close();
        return (list);
    }

    // method to return the list of velocity vectors of a given Body (ID)
    public static List<Vector3dInterface> getCSVVelocities(double ID) throws FileNotFoundException {
        ArrayList<Vector3dInterface> list = new ArrayList<Vector3dInterface>();
        FileReader f = new FileReader(fileName);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader, 1);

        while (Double.parseDouble((Reader.next())) != ID) {
            skipLine(Reader, 1);
            skipColumn(Reader, 1);
        }

        skipColumn(Reader, 3);

        while (Reader.hasNextLine()) {
            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x, y, z));

            skipLine(Reader, n_bodies);
            skipColumn(Reader, 5);
        }
        Reader.close();
        return (list);
    }

    public static int getFinalProbePosition() throws FileNotFoundException {
        FileReader f = new FileReader(trajectoryFile);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
       // skipColumn(Reader,1);
        double ind = Double.parseDouble(Reader.next());
        Reader.close();
        return (int) ind;
    }

    public static Vector3dInterface[] getProbeTrajectory() throws IOException {
        BufferedReader bfReader = new BufferedReader(new FileReader(trajectoryFile));
        int lines = 0;
        while (bfReader.readLine() != null)
            lines++;
        bfReader.close();
        // create array with enough space for every line (-2 cause of title line and
        // finalPos)
        Vector3dInterface[] trajectory = new Vector3dInterface[lines - 2];

        FileReader f = new FileReader(trajectoryFile);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 2);

        for (int i = 0; i < trajectory.length; i++) {
            //skipColumn(Reader, 1);
            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());
            trajectory[i] = new Vector(x, y, z);
        }
        Reader.close();
        return (trajectory);

    }

    public static List<List<Vector3dInterface>> getAllPositions() throws FileNotFoundException {
        List<List<Vector3dInterface>> allPositions = new ArrayList<List<Vector3dInterface>>();
        for (int i = 0; i < n_bodies; i++) {
            allPositions.add(getCSVPositions(i));
        }
        return allPositions;
    }

    // method to let Scanner skip line(s)
    public static void skipLine(Scanner s, int n) {
        for (int i = 0; i < n; i++) {
            if (s.hasNextLine()) {
                s.nextLine();
            }
        }
    }

    // method to let Scanner skip column(s)
    public static void skipColumn(Scanner s, int n) {
        for (int i = 0; i < n; i++) {
            if (s.hasNext()) {
                s.next();
            }
        }
    }

}
