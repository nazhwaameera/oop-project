package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import javax.swing.JPanel;

public class GameHeaderPanel extends JPanel{
	
	 private final int width, height;
	    String difficulty, brickDestroyed, highScore;
	    Graphics2D g2d;

	    GameHeaderPanel(int width, int height) {
	        this.width = width;
	        this.height = 15 * 2;
	    }

	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(width, height);
	    }

	    @Override
	    public Color getBackground() {
	        return Color.white;
	    }

	    /**
	     * Ensures GUI is painted when the window is moved or hidden.
	     */
	    @Override
	    public void paintComponent(Graphics g) {

	        super.paintComponent(g);
	        g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        paintHeader();

	    }

	    public void paintHeader() {
	        g2d.setColor(Color.white);
	        Font font = new Font("Monospaced", Font.PLAIN, 15);
	        FontRenderContext frc = g2d.getFontRenderContext();
	        GlyphVector gv = font.createGlyphVector(frc,
	                "Difficulty: " + difficulty + "    High Score: " + highScore
	                + "    Brick(s) Destroyed: " + brickDestroyed);
	        g2d.drawGlyphVector(gv,
	                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
	                height / 2 - ((int) gv.getVisualBounds().getHeight() / 2));
	    }

	    public void update(String difficulty, int highScore, int brickDestroyed) {
	        this.difficulty = difficulty;
	        this.highScore = String.valueOf(highScore);
	        this.brickDestroyed = String.valueOf(brickDestroyed);
	    }

}
