package id.ac.its.nazhwa133.guiproject1;

import javax.swing.JOptionPane;

public class CircleP1 {

	public static void main(String [] args) {
		
		// obtain user input from JOptionPane input dialog
			String radius1 = 
			JOptionPane.showInputDialog("Enter radius");
			
			//convert String inputs t int value for use in a calculation
			double radius = Double.parseDouble(radius1);
			
			double area = 3.14 * radius * radius;
			double circumference = 3.14 * 2 * radius;
			
			//display result in a JOpionPane message dialog
			JOptionPane.showMessageDialog(null, "The area is " + area, 
					"Area of a Circle", JOptionPane.PLAIN_MESSAGE);
			
			JOptionPane.showMessageDialog(null,"the circumference is" + circumference, 
					"Circumference of a Circle", JOptionPane.PLAIN_MESSAGE);
	}	
}
