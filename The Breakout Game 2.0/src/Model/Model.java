package Model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import View.View;
import View.Sprite;
import View.Ball;
import View.Brick;
import View.Paddle;
import Util.Direction;

public final class Model {

	private final int WIDTH = 300;
	private final int HEIGHT = 400;
	
	private int bricksDestroyed = 0;
	private Direction direction;
	private int difficulty;
	
	private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;
	private final View view;
	private Clip gameOverSound, eatAppleSound, gameMusicSound;
	
	private final String[] difficulties = {"Easy", "Medium", "Hard"};
	
	private final String HIGH_SCORE_EASY = "High Score " + difficulties[0] + ": " ;
	private final String HIGH_SCORE_MEDIUM = "High Score " + difficulties[1] + ": ";
	private final String HIGH_SCORE_HARD = "High Score " + difficulties[2] + ": ";
	private final String GAMES_PLAYED = "Games Played: ";
	private final String BRICKS_DESTROYED = "Bricks Destroyed: ";
	
	private final String dataID[] = {HIGH_SCORE_EASY, HIGH_SCORE_MEDIUM, 
			HIGH_SCORE_HARD, GAMES_PLAYED, BRICKS_DESTROYED};
	
	private final int[] data = new int[dataID.length];
	
	private final int TOTAL_GAMES_PLAYED_LOC = 3;
	private final int TOTAL_BRICKS_DESTROYED = 4;
	
	public Model() {                                                                
		loadData();
		view = new View(WIDTH, HEIGHT, paddle, ball, bricks);
		initSounds();
	}
	
	public void movePaddle( ) {

		int x = 0, dx = 0;

		x += dx;
		
		if (x <= 0) {

            x = 0;
        }

        if (x >= 300 - 40) {

            x = 300 - 40;
        }

		switch (direction) {
		case LEFT:
			dx = -1;
			break;

		case RIGHT:
			dx = 1;
			break;
		
		default:
            break;
            
		}
		
		if(ball.getRect().getMaxY() > 390) {
			stopMusic();
			playGameOverSound();
			data[difficulty] = Math.max(bricksDestroyed, data[difficulty]);
			view.update(difficulties[difficulty], bricksDestroyed, data[difficulty]);
			view.gameOver();
		}

		for (int i = 0, j = 0; i < 30; i++) {

			if (bricks[i].isDestroyed()) {

				j++;
			}

			if (j == 30) {
				stopMusic();
				playGameOverSound();
				data[difficulty] = Math.max(bricksDestroyed, data[difficulty]);
				view.update(difficulties[difficulty], bricksDestroyed, data[difficulty]);
				view.gameWin();
			}
		}

		if ((ball.getRect()).intersects(paddle.getRect())) {
			
			playEatAppleSound();
			int paddleLPos = (int) paddle.getRect().getMinX();
			int ballLPos = (int) ball.getRect().getMinX();

			int first = paddleLPos + 8;
			int second = paddleLPos + 16;
			int third = paddleLPos + 24;
			int fourth = paddleLPos + 32;

			if (ballLPos < first) {

				ball.setXDir(-1);
				ball.setYDir(-1);
			}

			if (ballLPos >= first && ballLPos < second) {

				ball.setXDir(-1);
				ball.setYDir(-1 * ball.getYDir());
			}

			if (ballLPos >= second && ballLPos < third) {

				ball.setXDir(0);
				ball.setYDir(-1);
			}

			if (ballLPos >= third && ballLPos < fourth) {

				ball.setXDir(1);
				ball.setYDir(-1 * ball.getYDir());
			}

			if (ballLPos > fourth) {

				ball.setXDir(1);
				ball.setYDir(-1);
			}
		}

		for (int i = 0; i < 30; i++) {

			if ((ball.getRect()).intersects(bricks[i].getRect())) {
				
				playEatAppleSound();
				int ballLeft = (int) ball.getRect().getMinX();
				int ballHeight = (int) ball.getRect().getHeight();
				int ballWidth = (int) ball.getRect().getWidth();
				int ballTop = (int) ball.getRect().getMinY();

				var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
				var pointLeft = new Point(ballLeft - 1, ballTop);
				var pointTop = new Point(ballLeft, ballTop - 1);
				var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

				if (!bricks[i].isDestroyed()) {

					if (bricks[i].getRect().contains(pointRight)) {

						ball.setXDir(-1);
					} else if (bricks[i].getRect().contains(pointLeft)) {

						ball.setXDir(1);
					}

					if (bricks[i].getRect().contains(pointTop)) {

						ball.setYDir(1);
					} else if (bricks[i].getRect().contains(pointBottom)) {

						ball.setYDir(-1);
					}

					bricks[i].setDestroyed(true);
				}
			}
		}
}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void newGame() {
		view.newGame();
	}
	
	public void continueGame() {
		clearModel();
		bricks = new Brick[30];
        ball = new Ball();
        paddle = new Paddle();
        playMusic();
        view.updateView(paddle, ball, bricks, difficulties[difficulty], data[difficulty], bricksDestroyed);
        view.continueGame();
		data[TOTAL_GAMES_PLAYED_LOC]++;
		saveData();
	}
	
	public void chooseDifficulty() {
		view.chooseDifficulty();
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public void clearModel() {
		bricksDestroyed = 0;
	}
	
	private void initSounds() {
        try {
            URL url = this.getClass().getClassLoader().getResource("sound/gameOver.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(audioIn);

            url = this.getClass().getClassLoader().getResource("sound/eatApple.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            eatAppleSound = AudioSystem.getClip();
            eatAppleSound.open(audioIn);

            url = this.getClass().getClassLoader().getResource("sound/gameMusic.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            gameMusicSound = AudioSystem.getClip();
            gameMusicSound.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public void pauseMusic() {
        if (gameMusicSound.isRunning()) {
            gameMusicSound.stop();
        } else {
            gameMusicSound.loop(100);
            gameMusicSound.start();
        }
    }

    public void playMusic() {
        gameMusicSound.setMicrosecondPosition(0);
        gameMusicSound.loop(100);
        gameMusicSound.start();
    }

    public void stopMusic() {
        gameMusicSound.stop();
    }

    public void playGameOverSound() {
        gameOverSound.setMicrosecondPosition(0);
        gameOverSound.start();
    }

    public void playEatAppleSound() {
        eatAppleSound.setMicrosecondPosition(0);
        eatAppleSound.start();
    }
    
    public void loadData() {
        Path path = Paths.get("./BreakoutData.txt");
        String line;
        int dataIndex = 0;
        try (InputStream in = Files.newInputStream(path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                data[dataIndex] = Integer.parseInt(line.replaceAll(dataID[dataIndex], ""));
                dataIndex++;
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveData() {
        for (int datum : data) {
            System.out.println(datum);
        }
        Path path = Paths.get("./BreakoutData.txt");
        try (OutputStream out = Files.newOutputStream(path)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            for (int i = 0; i < data.length; i++) {
                writer.write(dataID[i] + Integer.toString(data[i]));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void quit() {
        saveData();
        System.exit(0);
    }
	
	
}
