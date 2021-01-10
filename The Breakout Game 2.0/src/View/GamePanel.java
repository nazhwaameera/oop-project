package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GamePanel extends JPanel{
	
	private Paddle paddle;
	private Ball ball;
	private Brick[] bricks;
	private final int width, height;
	private Graphics2D g2d;
	
	GamePanel(int width, int height, Paddle paddle, Ball ball, Brick[] bricks) {
        this.width = width;
        this.height = height;
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
    }
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
	
	@Override
	public Color getBackground() {
		return Color.white;
	}

	@Override
	public boolean isOpaque() {
		return true;
	}
	
	@Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintPaddle();
        paintBall();
        paintBrick();

    }
	
	public void setGame(Paddle paddle, Ball ball, Brick[] bricks) {
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
    }
	
	public void paintPaddle() {
		 g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
	                paddle.getImageWidth(), paddle.getImageHeight(), this);
	}
	
	public void paintBall() {
		g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                ball.getImageWidth(), ball.getImageHeight(), this);
	}
	
	public void paintBrick() {
		for (int i = 0; i < 30; i++) {

            if (!bricks[i].isDestroyed()) {

                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getImageWidth(),
                        bricks[i].getImageHeight(), this);
            }
        }
		
	}
}
