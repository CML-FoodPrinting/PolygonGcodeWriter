//Creative Machines Lab| FoodPrinting.Software Spring 2018
//Authors: Sarah Yuan + Tutch Winyarat| sxy2003@columbia.edu sw3113@columbia.edu
//GcodeWriter class outputs a gcode file. 
//With the current implementation, a GcodeWriter allows writing instructions for printing a polygonal frame 
//and for filling in a polygonal layer.

/*
 * Implementation required the following methods:
 * 1. pickMaterial(int Material): pick a material from the material rack
 * 2. dropMaterial(int Material): drop the current material onto the rack
 * 3. returnHome(): move toolHead to home after done printing
 * 4. getUserInput(HashMap<> settings): initialize class variables with inputs from GUI
 */
import java.lang.Math;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GcodeWriter {

	////////// class variables begin here///////
	final double PI = Math.PI;
	static double travel_speed = 6000.0D;// inherited from legacy code
	static double layer_height = 1.2; // according to default in GUI
	static double z_lift = 20.0D;

	static double print_speed; // not used; user input
	static double unit_E; // one unit on the filament

	static double spacing;// distance between 2 nested frames
	static double twist_angle;// not used; handled by main()
	static int total_num_layers;// not used; handled by main()
	static double centre_to_side_length;// not used; handled by Polygon.java
	static double x_center;// not used; handled by Polygon.java
	static double y_center;// not used; handled by Polygon.java
	static double side_count;// not used; handled by Polygon.java

	static int top_thickness;// not used; user input
	static int bottom_thickness;// not used; user input
	static int bottom_layers;// not used; user input
	static double bed_z; // not used; user input
	static double cook_y_offset;// not used: user input
	static double cook_temp; // not used: user input
	static double retraction;// not used: user input
	static double cook_lift;// not used: user input
	static double cook_temp_standby;// not used: user input
	static double cook_speed_outer;// not used: user input
	static double cook_lap_on_fill;// not used: user input
	static double retract_after_dump = 3.0D;// not used: user input
	static double nozzle_dia = 1.8D;
	static double extrusion_width = 1.5D * nozzle_dia;
	static double E = 0; // global double E that tracks current coordinate of
							// the coordinate
	static File file;
	static FileWriter outPut;
	////////// class variables end here///////

	////// *****Constructor creates a file named fileName
	public GcodeWriter(String fileName) throws IOException // must end in .gcode
	{
		// add .gcode extension
		String name = fileName + ".gcode";
		// fileName is the name of a file to write the gCode to
		// open a file to write to
		file = new File(name); // create a new .gcode file
		outPut = new FileWriter(file);
	}

	// initialize triangle of 3 sides and radius of 30mm
	// 3 and 30 are hardcoded in for now.

	public void initFile() throws IOException {
		outPut.write("G21\n"); // set units to mm
		outPut.write("G90\n"); // set to absolute postioning
		outPut.write("M82\n"); // set extruder to absolute mode
		outPut.write(String.format("G01 F%4.2f\n", new Object[] { Double.valueOf(travel_speed) }));
		outPut.write("G92 E0\n"); // set position and mm of filament the
									// extruder extrudes = 0
		outPut.write("G28 X Y Z\n"); // move to origin
		outPut.write(String.format("G01 E%4.2f\n", new Object[] { Double.valueOf(0.0D) }));
	}

	// NOTE: E is currently incremented by sideLength without the unit_E factor
	// NOTE: printFrame() currents prints at travel_speed not print_speed;
	// print_speed is a user input
	public void printFrame(Polygon p, double height) throws IOException {
		int numOfSides = p.getSideCount();

		// move to frame's first vertex
		if (height < z_lift) // clear z lift then go to first vertex
		{
			double xCoord = p.getVertices().get(0)[0];
			double yCoord = p.getVertices().get(0)[1];
			double lift = Double.valueOf(z_lift);
			double speed = Double.valueOf(travel_speed);
			outPut.write(
					String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f\n", new Object[] { xCoord, yCoord, lift, speed }));
			outPut.write(String.format("G01 Z%4.2f  F%4.2f\n", new Object[] { height, travel_speed }));
		} else { // go directly to the first vertex
			double xCoord = p.getVertices().get(0)[0];
			double yCoord = p.getVertices().get(0)[1];
			double speed = Double.valueOf(travel_speed);
			outPut.write(
					String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f\n", new Object[] { xCoord, yCoord, height, speed }));
		}

		// this loop starts with moving to the first vertex
		// global E is already initialized to 0 before the loop begins
		for (int i = 0; i < numOfSides; i++) {
			E += p.getSideLength();
			double xCoord = p.getVertices().get((i + 1) % numOfSides)[0]; 
			// the 1-offset is required because we want to move to the 2nd vertex and back to the 1st																		
			double yCoord = p.getVertices().get((i + 1) % numOfSides)[1];
			double speed = Double.valueOf(travel_speed);
			outPut.write(String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f E%4.2f\n",
					new Object[] { xCoord, yCoord, height, speed, E }));
		}
	}

	///////// ************************* printFrame() ends here
	///////// *****************/////////

	//////// ***********************fillLayer() begins here

	// At height, fillLayer() prints a filled-in polygon p layer
	// NOTE: printFrame() currents prints at travel_speed not print_speed;
	// print_speed is a user input
	public void fillLayer(Polygon p, double height) throws IOException {
		int numOfSides = p.getSideCount();
		if (height < z_lift) // clear z lift then go to first vertex
		{
			double xCoord = p.getVertices().get(0)[0];
			double yCoord = p.getVertices().get(0)[1];
			double lift = Double.valueOf(z_lift);
			double speed = Double.valueOf(travel_speed);
			outPut.write(
					String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f\n", new Object[] { xCoord, yCoord, lift, speed }));
			outPut.write(String.format("G01 Z%4.2f  F%4.2f\n", new Object[] { height, speed }));
		} else { // go directly to the first vertex
			double xCoord = p.getVertices().get(0)[0];
			double yCoord = p.getVertices().get(0)[1];
			double speed = Double.valueOf(travel_speed);
			outPut.write(
					String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f\n", new Object[] { xCoord, yCoord, height, speed }));
		}

		spacing = extrusion_width - layer_height * 0.21460183660255172D;
		double currentRadius = p.getRadius();
		double shrinkFactor;
		// output
		int i = 1; // i is the the ith vertex we want to go to
		// int count = 1;

		/// print nested frames until the radius of the recently drawn frame is
		/// less than spacing between each frame
		while (currentRadius > spacing) {
			// Given a polygon with n sides, print the first (n-1) sides
			if ((i % numOfSides) != 0) // hardcoded 3 == number of polygon's
										// sides
			{
				E += p.getSideLength();
				double xCoord = p.getVertices().get(i % numOfSides)[0];
				double yCoord = p.getVertices().get(i % numOfSides)[1];
				outPut.write(String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f E%4.2f\n",
						new Object[] { xCoord, yCoord, Double.valueOf(height), Double.valueOf(travel_speed), E }));
				i++;
			}
			// print the n_th side of polygon, then jump (without printing) to
			// the first vertex of the next nested frame
			// Note that "without printing" simply means we do NOT increment the
			// E filament-coordinate
			else {
				E += p.getSideLength();
				double xCoord = p.getVertices().get(0)[0];
				double yCoord = p.getVertices().get(0)[1];
				double speed = Double.valueOf(travel_speed);
				outPut.write(String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f E%4.2f\n",
						new Object[] { xCoord, yCoord, Double.valueOf(height), speed, E }));

				// update radius
				currentRadius = p.getRadius();
				shrinkFactor = (currentRadius - spacing) / currentRadius;
				p.scale(shrinkFactor);

				// move to the first vertex of the nest nested frame
				xCoord = p.getVertices().get(0)[0];
				yCoord = p.getVertices().get(0)[1];
				outPut.write(String.format("G01 X%4.2f Y%4.2f Z%4.2f F%4.2f E%4.2f\n",
						new Object[] { xCoord, yCoord, Double.valueOf(height), speed, E }));
				i = 1;
			}
			// update radius
			currentRadius = p.getRadius();
		}
	}
	//////// ***********************fillLayer() ends here

	public void closeFile() throws IOException {
		outPut.close();
	}

}
