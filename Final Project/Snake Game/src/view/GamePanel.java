package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Deque;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Displayed while the game is being played. Responsible for the look and feel
 * of the game, as well as drawing the snake and apple. Methods exist for a grid
 * layout and a dot layout.
 *
 * @author Justin Beringer
 */
public class GamePanel extends JPanel {
	
	private boolean isEatingApple;
	private boolean isEatingOrange;
	private boolean isEatingRottenApple;
    private Deque<Point> snakeBody;
    private final Point apple;
    private final Point orange;
    private final Point rottenapple;
    private final int width, height, scale;
    private int r = 0;
    private int g = 255;
    private int b = 0;
    private final Random random = new Random();
    private Color snakeColor = new Color(0, 255, 0);
    private Graphics2D g2d;
    String difficulty, score, highScore, applesEaten, orangesEaten, rottenApplesEaten;

    GamePanel(int width, int height, int scale, Deque<Point> snakeBody, Point apple, Point orange, Point rottenapple
    		,String difficulty, int score, int highScore, int applesEaten, int orangesEaten, int rottenApplesEaten
    		, boolean isEatingApple, boolean isEatingOrange, boolean isEatingRottenApple) {
        this.snakeBody = snakeBody;
        this.width = (width - 400);
        this.height = height;
        this.scale = scale;
        this.apple = apple;
        this.orange = orange;
        this.rottenapple = rottenapple;
        this.difficulty = difficulty;
        this.score = String.valueOf(score);
        this.highScore = String.valueOf(highScore);
        this.applesEaten = String.valueOf(applesEaten);
        this.orangesEaten = String.valueOf(orangesEaten);
        this.rottenApplesEaten = String.valueOf(rottenApplesEaten);
        this.isEatingApple = isEatingApple;
        this.isEatingOrange = isEatingOrange;
        this.isEatingRottenApple = isEatingRottenApple;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Color getBackground() {
        return Color.black;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * Ensures GUI is painted when the window is moved or hidden.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintDots();
        paintGrid();
        paintApple();
        paintOrange();
        paintRottenApple();
        paintSnake();
        
        g2d.setColor(Color.red);
        g2d.drawRect(0,0,600,600);
        
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 14));
        if(isEatingApple) {
       
            g2d.setColor(Color.orange);
            g2d.drawString("Your snake eat an apple! " ,660,72);
            isEatingApple = false;
        	isEatingOrange = false;
        	isEatingRottenApple = false;
        }
        else if(isEatingOrange) {
        	g2d.setColor(Color.orange);
            g2d.drawString("Your snake eat an orange! " ,660,72);
            isEatingApple = false;
        	isEatingOrange = false;
        	isEatingRottenApple = false;
        }
        else if(isEatingRottenApple) {
        	g2d.setColor(Color.red);
            g2d.drawString("Your snake eat a rotten apple! " ,660,72);
            isEatingApple = false;
        	isEatingOrange = false;
        	isEatingRottenApple = false;
        }
        else {
        	
            g2d.drawString(" " ,660,72);
            isEatingApple = false;
        	isEatingOrange = false;
        	isEatingRottenApple = false;
        }
       
        g2d.setColor(Color.green);
        g2d.drawRect(620,20,360,560);
        g2d.drawRect(650, 50, 300, 35);
        
        g2d.setColor(Color.orange);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 30));
        g2d.drawString("SCORE",748,140);
        
        g2d.setColor(Color.gray);
        g2d.drawRect(700, 160, 200, 50);
        
        g2d.setFont(new Font("Tahoma", Font.BOLD, 40));
        g2d.setColor(Color.green);
        g2d.drawString(" " + score,715,199);
        
        g2d.setColor(snakeColor);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 15));
        g2d.drawString("STATUS", 650, 250);
        g2d.setColor(Color.orange);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
        g2d.drawString("Difficulty    : "+ difficulty, 670, 275);
        g2d.drawString("High Score : " + highScore, 670, 300);
        g2d.drawString("Apples Eaten :" + applesEaten, 670, 325);
        g2d.drawString("Oranges Eaten :" + orangesEaten, 670, 350);
        g2d.drawString("Rotten Apples Eaten :" + rottenApplesEaten, 670, 375);
        
        g2d.setColor(snakeColor);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 15));
        g2d.drawString("MORE INFO", 650, 475);
        g2d.setColor(Color.orange);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
        g2d.drawString("Eat Red Apple              : ", 670, 500);
        g2d.drawString("Eat Orange                   : ", 670, 525);
        g2d.drawString("Eat Rotten Apple         : ", 670, 550);
       
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g2d.setColor(snakeColor);
        g2d.drawString("Score +2", 850, 500);
        g2d.drawString("Score +3", 850, 525);
        g2d.setColor(Color.red);
        g2d.drawString("Score -", 850, 550);
        
        g2d.setColor(snakeColor);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 15));
        g2d.drawString("CONTROL", 650, 400);
        g2d.setColor(Color.orange);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
        g2d.drawString("Move with Arrow Key", 670, 425);
        g2d.drawString("P for Pause/Resume", 670, 450);
        
    }

    public void setSnakeBody(Deque<Point> snakeBody, Point apple, Point orange, Point rottenapple) {
        this.snakeBody = snakeBody;
    }

    public void paintApple() {
        g2d.setStroke(new BasicStroke(1.5f));
        int xPos = (int) apple.getX();
        int yPos = (int) apple.getY();
        g2d.setColor(Color.green);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale / 2, yPos - 1);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale - 1, yPos - 2);
        g2d.setColor(Color.red);
        g2d.fillOval(xPos + 2, yPos + 2, scale - 4, scale - 3);
        g2d.fillOval(xPos + 4, yPos + 2, scale - 4, scale - 3);
    }
    
    public void paintOrange() {
        g2d.setStroke(new BasicStroke(1.5f));
        int xPos = (int) orange.getX();
        int yPos = (int) orange.getY();
        g2d.setColor(Color.green);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale / 2, yPos - 1);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale - 1, yPos - 2);
        g2d.setColor(Color.orange);
        g2d.fillOval(xPos + 2, yPos + 2, scale - 4, scale - 3);
        g2d.fillOval(xPos + 4, yPos + 2, scale - 4, scale - 3);
    }
    
    public void paintRottenApple() {
    	g2d.setStroke(new BasicStroke(1.5f));
        int xPos = (int) rottenapple.getX();
        int yPos = (int) rottenapple.getY();
        g2d.setColor(Color.green);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale / 2, yPos - 1);
        g2d.drawLine(xPos + scale / 2, yPos, xPos + scale - 1, yPos - 2);
        g2d.setColor(Color.darkGray);
        g2d.fillOval(xPos + 2, yPos + 2, scale - 4, scale - 3);
        g2d.fillOval(xPos + 4, yPos + 2, scale - 4, scale - 3);
    }

    public void paintSnake() {
        int xPos, yPos;
        for (Point position : snakeBody) {
            g2d.setColor(snakeColor);
            xPos = (int) position.getX();
            yPos = (int) position.getY();
            g2d.drawRoundRect(xPos + 2, yPos + 2, scale - 4, scale - 4, 2, 2);
        }
        System.out.println(snakeColor);
        r = 0;
        g = 255;
        b = 0;
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
    
    public void update(String difficulty, int highScore, int score, int applesEaten, int orangesEaten, int rottenApplesEaten
    		, boolean isEatingApple, boolean isEatingOrange, boolean isEatingRottenApple) {
        this.difficulty = difficulty;
        this.highScore = String.valueOf(highScore);
        this.score = String.valueOf(score);
        this.applesEaten = String.valueOf(applesEaten);
        this.orangesEaten = String.valueOf(orangesEaten);
        this.rottenApplesEaten = String.valueOf(rottenApplesEaten);
        this.isEatingApple = isEatingApple;
        this.isEatingOrange = isEatingOrange;
        this.isEatingRottenApple = isEatingRottenApple;
    }
   
}