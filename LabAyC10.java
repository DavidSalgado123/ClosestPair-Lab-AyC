/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labayc10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/*
 * Algorithms and Complexity                                  November 04, 2022
 * IST 4310-01
 * Prof. M. Diaz-Maldonado
 *
 * Laboratory 2: Calculating time complexity in the Find Closest Pair Algorithm
 *
 * Synopsis: Creates a file with n random integers, then all those arrays are transfered to an array.
 * The program reads each element in the array and stores the unique numbers in a different array. It also counts the amount of times each
 * unique number appears, and prints the information to the user.
 * This process is done at least 200 times for every file with n integers in order to find the average runtime for the program given.
 * It also forces the best case (all n integers are 0) to analize it and compare it to the average and worst case (n different numbers).
 * 
 * Author: David Salgado Cortés
 * ID: 200144831
*/
class Point { //Define a Point class with x and y positions inside a position. 

        int x;
        int y;
        int pos;

        public Point(int xx, int yy, int poss) {
            this.x = xx;
            this.y = yy;
            this.pos = poss;
        }
    }
public class LabAyC10 {

    /**
     * @param args the command line arguments
     */
    
    static int conditionalchecks;
    
    public static void main(String[] args) { //This is the main block where the execution of the algorithm is developed, the first loops is to create the sets of coordinates from 500 to 1000000, in which there is another loop to repeat the process 200 times per set. The arraylists created let me know how much conditional checks were made and how much time it took for the algoritm to execute
        ArrayList<Integer> times = new ArrayList<Integer>();
        ArrayList<Integer> counters = new ArrayList<Integer>();
        ArrayList<Integer> amountn = new ArrayList<Integer>();
        for(int i=500; i<1000000; i=i*3/2){
            long totaltime =0;           
            ArrayList<Point> newCoords = new ArrayList<Point>();
            RandomCoords(i,newCoords);
            newCoords.sort(Comparator.comparing(Point -> Point.x));
            for(int j=0; j<200;j++){
                long startTime=System.nanoTime();
                Divide(newCoords,i,999999999);
                long endTime=System.nanoTime();
                long Time = endTime-startTime;
                totaltime= totaltime + Time;
            }
            int counter = conditionalchecks/256;
            totaltime=totaltime/256;
            times.add((int)totaltime);
            counters.add((int)counter);
            amountn.add(i);    
        }
            create("ResultAyCLab.txt");
            write("ResultAyCLab.txt",amountn.size(),amountn,counters,times);
        
    }
    
    public static double distance(List<Point> x,int i, int j){ //Let me know the distance between 2 points base on their position in x and y
        int x1=x.get(i).x;
        int x2=x.get(j).x;
        int y1=x.get(i).y;
        int y2=x.get(j).y;
        double d = sqrt ( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );
        return d;
    }
    
    public static double[] ClosestPair(int n, List<Point> x, double mind){ //Brute Force Algorithm provided by the teacher and adapted to my algorithm
        double d_minimun=mind;
        double[] variables = new double[3];
        variables[0] = d_minimun;
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                double d = distance(x, i, j);
                if(d < d_minimun){
                    conditionalchecks++;
                    d_minimun=d;
                    variables[0]=d;
                    variables[1] = x.get(i).pos;
                    variables[2] = x.get(j).pos;
                }else{
                   conditionalchecks++; 
                }           
            }
        }
        return variables;
    }
    
    public static double[] Divide(List<Point> x, int n, double mind){ //Divide the coordinates and look for the closest pair
        if(n>3){
          conditionalchecks++;
          double[] Procedure1 = new double[3];         
          double[] Procedure2 = new double[3];
          int odd=0;
          if(n%2!=0){
              odd=1;
          }
          Procedure1 = Divide(x.subList(0, n/2),n/2,mind);
          Procedure2 = Divide(x.subList(n/2,n),n/2 + odd,mind);
          double[] dis_min = new double[3];
          if(Procedure1[0]>Procedure2[0]){
             conditionalchecks++;
             dis_min=Procedure2; 
          }else{
              conditionalchecks++;
             dis_min=Procedure1;
          }
          ArrayList<Point> candidates = new ArrayList<Point>(); 
           candidates = FindCandidates(x, dis_min[0]);            
            if(candidates.size() > 1){
                conditionalchecks++;
                Procedure1 = ClosestPair(candidates.size(), candidates, mind);
              if(Procedure1[0] < dis_min[0]){
                conditionalchecks++;  
                return Procedure1; 
             } else {
                 conditionalchecks++;
                 return dis_min;
             }
            } else {
                conditionalchecks++;
                return dis_min;
            } 
          
        }else{
            conditionalchecks++;
            double[] variables = new double[3];
            return ClosestPair(n,x,mind);
        }
        
        
    }
    
    public static ArrayList<Point> FindCandidates(List<Point> coordsx, double mind) { //After the set is divided search if in the borders, points from different subsets are closer to each other than inside the same subset
        ArrayList<Point> candidates = new ArrayList<Point>();
        int i = 0;
        while (i < coordsx.size() / 2) { 
            if (Math.abs(coordsx.get(i).x - coordsx.get(coordsx.size() / 2).x) < mind && Math.abs(coordsx.get(i).y - coordsx.get(coordsx.size() / 2).y) < mind) {
                conditionalchecks++;
                candidates.add(coordsx.get(i)); 
                i++;
            } else {
                conditionalchecks++;
                i = coordsx.size() / 2;
            }
        }       
        return candidates;
    }
    
    public static ArrayList<Point> RandomCoords(int n,ArrayList<Point> newCoords) { //Creates random coordinates for X and Y of values between 0 and 10000.
 
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            Point randcoords = new Point(rand.nextInt(10000), rand.nextInt(10000), i);
            newCoords.add(randcoords);
        }

        return newCoords;
    }
    
     private static void create (String name) 
	// creates a file with a given name
	{
		try
		{
			// defines the filename
			String fname = (name);
			// creates a new File object
			File f = new File (fname);
                        
			String msg = "creating file `" + fname + "' ... ";
			// creates the new file
			f.createNewFile();

		}
		catch (IOException err)
		{
			// complains if there is an Input/Output Error
			err.printStackTrace();
		}

		return;
	}


        private static void write (String name, int size, ArrayList<Integer> amountn, ArrayList<Integer> counters, ArrayList<Integer> times)
	// writes data to a file with a given name and size
	{
		try
		{
			// defines the filename
			String filename = (name);
			// creates new PrintWriter object for writing file
			PrintWriter out = new PrintWriter (filename);
                        String fmt = ("%10s %10s %10s\n"); 
                            for (int i = 0; i < size; ++i){
                                out.printf(fmt, amountn.get(i), counters.get(i), times.get(i));
                            }
			

			out.close();	// closes the output stream
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}

		return;
	}
    
}
