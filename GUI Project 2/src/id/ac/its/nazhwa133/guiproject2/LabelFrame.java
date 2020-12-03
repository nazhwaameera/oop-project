package id.ac.its.nazhwa133.guiproject2;

import java.awt.FlowLayout; // specifies how components are arranged
import java.awt.Image;
import javax.swing.JFrame; // provides basic window features
import javax.swing.JLabel; // displays text and images
import javax.swing.SwingConstants; // common constants used with Swing
import javax.swing.Icon; // interface used to manipulate images
import javax.swing.ImageIcon; // loads images

public class LabelFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private final JLabel label;
	
	public LabelFrame() {
		super("Data Diri Mahasiswa");
		setLayout(new FlowLayout());
		
		// JLabel constructor with string, Icon and alignment arguments
		Icon image = new ImageIcon(new ImageIcon("image/picture.jpeg").getImage().getScaledInstance(150, 200, Image.SCALE_DEFAULT));
		
		label = new JLabel(); // JLabel constructor no arguments
		label.setText("Nazhwa Ameera H. - 05111940000133");
		label.setIcon(image); // add icon to JLabel
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.BOTTOM);
		label.setToolTipText("Data Diri Mahasiswa");
		add(label); // add label to JFrame
	}
}
