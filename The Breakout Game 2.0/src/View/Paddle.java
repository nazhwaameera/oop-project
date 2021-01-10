package View;

import javax.swing.ImageIcon;

public class Paddle extends Sprite {
	
	private int dx;

	public Paddle() {

		initPaddle();
	}

	private void initPaddle() {

		loadImage();
		getImageDimensions();

		resetState();
	}

	public void loadImage() {

		var ii = new ImageIcon("src/resources/paddle.png");
		image = ii.getImage();
	}
	
	private void resetState() {

		x = 200;
		y = 360;
	}
}
