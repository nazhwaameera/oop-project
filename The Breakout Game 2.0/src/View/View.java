package View;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class View {
	private final GamePanel gamePanel;
	private final GameHeaderPanel gameHeaderPanel;
	private final ViewListener viewListener = new ViewListener();
	private final GameOverPanel gameOverPanel;
	private final GameWinPanel gameWinPanel;
	private final NewGamePanel newGamePanel;
	private final DifficultyPanel difficultyPanel;
	private JFrame frame;
	private JPanel content;
	private final List<Image> icons = new ArrayList<>();
	
	public View(int width, int height, Paddle paddle, Ball ball, Brick[] bricks) {
		gamePanel = new GamePanel(width, height, paddle, ball, bricks);
		newGamePanel = new NewGamePanel(width, height);
		gameOverPanel = new GameOverPanel(width, height);
		gameWinPanel = new GameWinPanel(width, height);
		difficultyPanel = new DifficultyPanel(width, height);
		gameHeaderPanel = new GameHeaderPanel(width, height);
		initIcons();
		initGridView();
	}
	
	private void initGridView() {
        frame = new JFrame("Breakout Game");
        frame.addKeyListener(viewListener);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setBackground(Color.black);
        content.add(gameHeaderPanel);
        content.add(gamePanel);
        frame.add(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        newGame();
        frame.setIconImages(icons);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateView(Paddle paddle, Ball ball, Brick[] bricks, String difficulty, int highScore, int bricksDestroyed) {
        gamePanel.setGame(paddle, ball, bricks);
        gameHeaderPanel.update(difficulty, highScore, bricksDestroyed);
        gameHeaderPanel.repaint();
        gamePanel.repaint();
    }

    public void gameOver() {
        System.out.println("GAME OVER");
        viewListener.setGameOver(true);
        content.removeAll();
        content.add(gameOverPanel);
        content.validate();
        content.repaint();
    }
    
    public void gameWin() {
    	System.out.println("YOU WIN!");
        viewListener.setGameWin(true);
        content.removeAll();
        content.add(gameWinPanel);
        content.validate();
        content.repaint();
    }

    public void newGame() {
        System.out.println("NEW GAME");
        viewListener.setNewGame(true);
        content.removeAll();
        content.add(newGamePanel);
        content.validate();
        content.repaint();
    }

    public void chooseDifficulty() {
        System.out.println("CHOOSE DIFFICULTY");
        viewListener.setChoosingDifficulty(true);
        content.removeAll();
        content.add(difficultyPanel);
        content.validate();
        content.repaint();
    }
    
    public void continueGame() {
        System.out.println("CONTINUE GAME");
        viewListener.setGameOver(false);
        viewListener.setNewGame(false);
        viewListener.setChoosingDifficulty(false);
        content.removeAll();
        content.add(gameHeaderPanel);
        content.add(gamePanel);
        content.validate();
        content.repaint();
    }

    public void update(String difficulty, int bricksDestroyed, int highScore) {
        gameOverPanel.update(difficulty, bricksDestroyed, highScore);
    }

    private void initIcons() {
        try {
            URL url = this.getClass().getClassLoader().getResource("icon/Icon_128.png");
            icons.add((new ImageIcon(url)).getImage());
        } catch (Exception e) {
        }
    }
}


