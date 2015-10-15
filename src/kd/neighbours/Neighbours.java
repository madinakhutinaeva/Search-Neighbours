package kd.neighbours;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;


public class Neighbours {

	 public static void main(String[] args) {
	        
	        if(args.length == 0){
	            System.out.println("Provide filename, please");
	            System.exit(-1);
	        }
	        String filename = args[0];
	        Scanner scan;
	        File file = new File(filename);
	        KdTree tree = new KdTree();
	        java.util.Queue<Double> queue = new LinkedList<>();
	        try {
	            scan = new Scanner(file);
	            scan.useLocale(Locale.ENGLISH);
	            while(scan.hasNextDouble())
	            {
	            	queue.add(scan.nextDouble());
		            if (queue.size() == 2){
		            	tree.insert(new Point2D(queue.poll(), queue.poll()));
		            }
	            }
	        } catch (FileNotFoundException e1) {
	                e1.printStackTrace();
	        }
	        double epsilon = 0.0000000000000001;
	        for(Point2D p: tree.getPoints()) {
	        	Set<Point2D> s = tree.getNeighours(p, epsilon);
	        	double radius = p.distanceTo(tree.nearest(p, epsilon));
	        	System.out.println(p + " radius: " + radius + "  neighbours: " +  s.size());

	        }
	        
	        
	 }
}
