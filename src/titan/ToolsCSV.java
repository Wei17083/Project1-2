package titan;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ToolsCSV {
    public static void createCSV(ArrayList<State> listStates) throws FileNotFoundException {
        File csvFile = new File("data.csv");
        PrintWriter out = new PrintWriter(csvFile);

        out.println("time, ID, X-Position, Y-Position, Z-Position, X-Velocity, Y-Velocity, Z-Velocity");

        for (State s : listStates) {
            for (int i = 0; i < s.positionList.size(); i++) {
                out.print(s.tState + ", "); //1st column: time
                out.print(i + ", ");    //2nd column: ID
                out.print(s.positionList.get(i).getX() + ", "); //3rd column: X-Position
                out.print(s.positionList.get(i).getY() + ", "); //4th column: Y-Position
                out.print(s.positionList.get(i).getZ() + ", "); //5th column: Z-Position
                out.print(s.velocityList.get(i).getX() + ", "); //6th column: X-Velocity
                out.print(s.velocityList.get(i).getY() + ", "); //7th column: Y-Velocity
                out.println(s.velocityList.get(i).getZ()+ ", "); ////8th column: Z-Velocity
            }
        }
        out.close();
    }

    //method to return the list of position vectors of a given Body (ID)
    public static List<Vector> getCSVPositions(String s, double ID) throws FileNotFoundException{
        LinkedList<Vector> list = new LinkedList<Vector>();
        FileReader f = new FileReader(s);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader, 1);

        while(Double.parseDouble((Reader.next())) != ID){
            skipLine(Reader, 1);
            skipColumn(Reader,1);
        }

        while(Reader.hasNextLine()){

            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x,y,z));

            skipLine(Reader,3);
            skipColumn(Reader, 2);
        }
        Reader.close();
        return(list);
    }

    //method to return the list of velocity vectors of a given Body (ID)
    public static List<Vector> getCSVVelocities(String s, double ID) throws FileNotFoundException{
        LinkedList<Vector> list = new LinkedList<Vector>();
        FileReader f = new FileReader(s);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader,1);

        while(Double.parseDouble((Reader.next())) != ID){
            skipLine(Reader, 1);
            skipColumn(Reader, 1);
        }

        skipColumn(Reader,3);

        while(Reader.hasNextLine()){
            //String test = Reader.next();
            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            //String test = Reader.next();
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x,y,z));

            skipLine(Reader,3);
            skipColumn(Reader, 5);
        }
        Reader.close();
        return(list);
    }

    //method to let Scanner skip line(s)
    public static void skipLine(Scanner s,int n){
        for(int i = 0; i < n;i++){
            if(s.hasNextLine()){
                s.nextLine();
            }
        }
    }

    //method to let Scanner skip column(s)
    public static void skipColumn(Scanner s,int n){
        for(int i = 0; i < n;i++){
            if(s.hasNext()){
                s.next();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        // state 1

        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        positionList.add(new Vector(1,1,1));
        positionList.add(new Vector(2,1,2));
        positionList.add(new Vector(5,6,5));

        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        velocityList.add(new Vector(-1,-1,-1));
        velocityList.add(new Vector(-2,-1,-2));
        velocityList.add(new Vector(-5,-6,-5));

        State s0 = new State(0, positionList, velocityList);

        // state 2

        ArrayList<Vector3dInterface> positionList1 = new ArrayList<>();
        positionList1.add(new Vector(2,2,2));
        positionList1.add(new Vector(3,2,3));
        positionList1.add(new Vector(6,7,6));

        ArrayList<Vector3dInterface> velocityList1 = new ArrayList<>();
        velocityList1.add(new Vector(-2,-2,-2));
        velocityList1.add(new Vector(-3,-2,-3));
        velocityList1.add(new Vector(-6,-7,-6));

        State s1 = new State(1, positionList1, velocityList1);

        //state 3

        ArrayList<Vector3dInterface> positionList2 = new ArrayList<>();
        positionList2.add(new Vector(3,3,3));
        positionList2.add(new Vector(4,3,4));
        positionList2.add(new Vector(7,8,7));

        ArrayList<Vector3dInterface> velocityList2 = new ArrayList<>();
        velocityList2.add(new Vector(-3,-3,-3));
        velocityList2.add(new Vector(-4,-3,-4));
        velocityList2.add(new Vector(-7,-8,-7));

        State s2 = new State(1, positionList2, velocityList2);


        ArrayList<State> listStates = new ArrayList<>();
        listStates.add(s0);
        listStates.add(s1);
        listStates.add(s2);

        createCSV(listStates);

        double usedID = 1;

        List<Vector> l = getCSVPositions("data.csv", usedID);
        System.out.println("Positions of Body " + usedID + ": " + l.toString());
        List<Vector> lv = getCSVVelocities("data.csv", usedID);
        System.out.println("Velocities of Body " + usedID + ": " + lv.toString());
    }
}
