package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import javax.swing.JPanel;

/**
 * Drawn to signify a Game Over due to collision.
 *
 * @author Justin Beringer
 */
public class GameOverPanel extends JPanel {

    private final int width, height, scale;
    int score, highScore;
    Graphics2D g2d;
    private String difficulty;

    GameOverPanel(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Color getBackground() {
        return Color.black;
    }

    /**
     * Ensures GUI is painted when the window is moved or hidden.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        paintDots();
        paintGrid();
        paintGameOver();
        paintDifficulty();
        paintScore();
        paintHighScore();
        paintPlayAgain();

    }

    public void paintGameOver() {
        g2d.setColor(Color.red);
        Font font = new Font("Monospaced", Font.BOLD, width / 10);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, "GAME OVER");
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height * 7 / 20 - ((int) gv.getVisualBounds().getHeight() / 2));
    }
    
    public void paintDifficulty() {
        g2d.setColor(Color.white);
        Font font = new Font("Monospaced", Font.BOLD, width / 25);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, "Difficulty: " + difficulty);
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height * 9 / 20 - ((int) gv.getVisualBounds().getHeight() / 2));
    }

    public void paintScore() {
        Font font = new Font("Monospaced", Font.BOLD, width / 25);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, "Score: " + score);
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height * 11 / 20 - ((int) gv.getVisualBounds().getHeight() / 2));
    }

    public void paintHighScore() {
        Font font = new Font("Monospaced", Font.BOLD, width / 25);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, "High Score: " + highScore);
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height * 13 / 20 - ((int) gv.getVisualBounds().getHeight() / 2));
    }

    public void paintPlayAgain() {
        g2d.setColor(Color.orange);
        Font font = new Font("Monospaced", Font.BOLD, width / 20);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, "Play Again? Y/N");
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height * 16 / 20 - ((int) gv.getVisualBounds().getHeight() / 2));
    }

    public void update(String difficulty, int score, int highScore) {
        this.difficulty = difficulty;
        this.score = score;
        this.highScore = highScore;
    }
    
    public void paintDots() {
        g2d.setStroke(new BasicStroke(0.25f));
        g2d.setColor(Color.gray);
        for (int i = 0; i <= width / scale; i++) {
            for (int j = 0; j <= height / scale; j++) {
                if (i * scale == width) {
                    g2d.fillRect(i * scale - 1, j * scale, 1, 1);
                } else if (j * scale == height) {
                    g2d.fillRect(i * scale, j * scale - 1, 1, 1);
                } else {
                    g2d.fillRect(i * scale, j * scale, 1, 1);
                }
            }
        }
        g2d.fillRect(width - 1, height - 1, 1, 1);

    }
    public void paintGrid() {
        g2d.setStroke(new BasicStroke(0.25f));
        g2d.setColor(Color.gray);
        for (int i = 1; i < width / scale; i++) {
            g2d.drawLine(i * scale, 0, i * scale, height);
        }
        for (int i = 1; i < height / scale; i++) {
            g2d.drawLine(0, i * scale, width, i * scale);

        }
    }

}