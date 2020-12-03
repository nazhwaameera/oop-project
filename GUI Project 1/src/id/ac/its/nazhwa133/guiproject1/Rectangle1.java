package id.ac.its.nazhwa133.guiproject1;

import javax.swing.JOptionPane;

public class Rectangle1 {

	public static void main(String [] args) {
		
		// obtain user input from JOptionPane input dialog
			String length1 = 
			JOptionPane.showInputDialog("Enter length");
			
			String width1 = 
					JOptionPane.showInputDialog("Enter width");
			
			//convert String inputs t int value for use in a calculation
			double length = Double.parseDouble(length1);
			double width = Double.parseDouble(width1);
			
			double area = length * width;
			double circumference = 2 * (length + width);
			
			//display result in a JOpionPane message dialog
			JOptionPane.showMessageDialog(null, "The area is " + area, 
					"Area of a Rectangle", JOptionPane.PLAIN_MESSAGE);
			
			JOptionPane.showMessageDialog(null,"the circumference is" + circumference, 
					"Circumference of a Rectangle", JOptionPane.PLAIN_MESSAGE);
	}	

}
