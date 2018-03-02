import java.io.IOException;
import java.util.ArrayList;

//This is a test engine for the Polygon class
public class TestEngine {

	/*
	 * public static void main(String arg[]) { test engine for a triangle
	 * //create a new polygon(triangle) Polygon triangle = new Polygon(3, 1);
	 * //expected: sqrt(3) = 1.73
	 * System.out.println("sidelength of a triangle of radius 1:" +
	 * triangle.getSideLength());
	 * 
	 * //translate triangle by (2,2) triangle.translate(2, 2);
	 * ArrayList<double[]> coords = triangle.getVertices(); //print out coords
	 * of vertex2 //expected: (1.5, 2.86)
	 * System.out.println("vertex2 coords after translating by 2: " +
	 * (coords.get(1)[0]) + " " + coords.get(1)[1]);
	 * 
	 * //translate back and scale by 10 //triangle.translate(-2, -2);
	 * triangle.scale(10); //print out coords of vertex2 coords =
	 * triangle.getVertices(); //expected: (-5, 8.66)
	 * System.out.println("vertex2 coords after scaling by 10: " +
	 * (coords.get(1)[0]) + " " + coords.get(1)[1]);
	 * 
	 * //scale back by 0.1 and rotate by pi triangle.scale(.1);
	 * triangle.rotate(Math.PI); coords = triangle.getVertices(); //expected:
	 * (0.5, -0.87)
	 * System.out.println("vertec2 coords after rotating CCW by pi: " +
	 * (coords.get(1)[0]) + " " + coords.get(1)[1] );
	 * System.out.println("Triangle's twistAngle: " + triangle.getAngle() +
	 * " degrees radian");
	 * 
	 * //expected: 0 degree triangle.rotate((3.0/3.0)*(Math.PI));
	 * System.out.println("Triangle's twistAngle: " + triangle.getAngle() +
	 * " degrees randian");
	 * 
	 * }
	 */

	public static void main(String arg[]) throws IOException {
		// predefined values
		double layer_height = 1.2; // input from GUI
		double bed_z = 6.0;// input from GUI (6.0 without a plate; print
							// directly onto bed)

		GcodeWriter writer = new GcodeWriter("test03_02");

		//////// ******** print triangle
		// create a triangle of radius 30mm, centered at (0,0)
		Polygon triangle = new Polygon(3, 30);
		// move triangle to (30,30)
		triangle.translate(40, 80);
		double currentHeight = 0.0;
		writer.initFile();
		// the first two layers are solid bases
		Polygon filledTriOne = new Polygon(triangle);
		Polygon filledTriTwo = new Polygon(triangle);
		for (int i = 0; i < 25; i++) {
			// currentHeight = Double.valueOf((i+1.0) * layer_height) + bed_z;
			// //figure out the currentHeight for the z coordinate
			currentHeight = currentHeight * .95 + 2.0;
			// if at 0th or 1th layer, print filled bases
			if (i == 0) {
				writer.fillLayer(filledTriOne, currentHeight);
			}
			/*
			 * else if( i == 1) { writer.fillLayer(filledTriTwo, currentHeight);
			 * }
			 */
			else {
				writer.printFrame(triangle, currentHeight);
				triangle.scale(0.875); // Math.exp(-100.0/25.0)
				triangle.rotate((1.0 / 40.0) * Math.PI);
			}
		}
		
		// ******** print square
		// create a square of radius 40mm
		Polygon square = new Polygon(4, 40);
		square.translate(100, 45);
		Polygon filledSquaOne = new Polygon(square);
		currentHeight = 0.0;

		for (int i = 0; i < 45; i++) {
			// currentHeight = Double.valueOf((i+1.0) * layer_height) + bed_z;
			// //figure out the currentHeight for the z coordinate
			currentHeight = currentHeight * .95 + 3.0;
			// if at 0th or 1th layer, print filled bases
			if (i == 0) {
				writer.fillLayer(filledSquaOne, currentHeight);
			}
			/*
			 * else if( i == 1) { writer.fillLayer(filledTriTwo, currentHeight);
			 * }
			 */
			else {
				writer.printFrame(square, currentHeight);
				square.scale(0.925); // Math.exp(-100.0/25.0)
				square.rotate((1.0 / 40.0) * Math.PI);
			}
		}

		// ******** print a plate
		// create a pentagon of radius 40mm
		Polygon pentagon = new Polygon(20, 10);
		pentagon.translate(50, 150);
		pentagon.rotate((1.0 / 5.0) * (Math.PI));
		Polygon filledPentaOne = new Polygon(pentagon);
		currentHeight = 0.0;

		for (int i = 0; i < 30; i++) {
			// currentHeight = Double.valueOf((i+1.0) * layer_height) + bed_z;
			// //figure out the currentHeight for the z coordinate
			currentHeight = currentHeight *0.95 + 1.0;
			// if at 0th or 1th layer, print filled bases
			if (i == 0) {
				writer.fillLayer(filledPentaOne, currentHeight);
			}
			/*
			 * else if( i == 1) { writer.fillLayer(filledTriTwo, currentHeight);
			 * }
			 */
			else
			{
				writer.printFrame(pentagon, currentHeight);
				pentagon.scale(1.05);
			}
			
		}

		// ******** print Hexagon
		// create a square of radius 35mm
		Polygon hexagon = new Polygon(6, 35);
		hexagon.translate(140, 150);
		hexagon.rotate((1.0 / 5.0) * (Math.PI));
		Polygon filledHexaOne = new Polygon(hexagon);
		currentHeight = 0.0;

		for (int i = 0; i < 40; i++) {
			// currentHeight = Double.valueOf((i+1.0) * layer_height) + bed_z;
			// //figure out the currentHeight for the z coordinate
			currentHeight = currentHeight * .95 + 3.0;
			// if at 0th or 1th layer, print filled bases
			if (i == 0) {
				writer.fillLayer(filledHexaOne, currentHeight);
			}
			/*
			 * else if( i == 1) { writer.fillLayer(filledTriTwo, currentHeight);
			 * }
			 */
			else {
				writer.printFrame(hexagon, currentHeight);
				hexagon.scale(0.9); // Math.exp(-100.0/25.0)
				hexagon.rotate((2.0 / 40.0) * Math.PI);
			}
		}

		
		//******** print dome
		// create a Hemisphere of radius 40mm
		Polygon circle = new Polygon(20, 30);
		circle.translate(160, 80);
		circle.rotate((1.0 / 5.0) * (Math.PI));
		currentHeight = 0.0;
		Polygon filledCirOne = new Polygon(circle);
		double r1 = Math.pow(circle.getRadius(), 2);
		double h = Math.pow(currentHeight, 2);
		double scaleFactor = (Math.sqrt(r1 - h))/r1;
		
		for (int i = 0; i < 40; i++) {
			// currentHeight = Double.valueOf((i+1.0) * layer_height) + bed_z;
			// //figure out the currentHeight for the z coordinate
			currentHeight = currentHeight*0.95 + 3.0;
			// if at 0th or 1th layer, print filled bases
			if (i == 0) {
				writer.fillLayer(filledCirOne, currentHeight);
			}
			/*
			 * else if( i == 1) { writer.fillLayer(filledTriTwo, currentHeight);
			 * }
			 */
			else {
				h = Math.pow(currentHeight, 2);
				scaleFactor = (Math.sqrt(r1 - h))/circle.getRadius();
				writer.printFrame(circle, currentHeight);
				circle.scale(scaleFactor); // Math.exp(-100.0/25.0)
			}
		}

		writer.closeFile();

	}

}
