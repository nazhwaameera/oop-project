package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import Model.Model;
import Util.Direction;

public class Controller {

	private static int TICKS_PER_SECOND;
	private static boolean buttonPressed = false;
	private static boolean isGameOver = false;
	private static boolean isGameWin = false;
	private static boolean isNewGame = true;
	private static boolean isPaused = false;
	private boolean isChoosingDifficulty;

	private static final Model model = new Model();
	private static ActionListener taskPerformer;
	private static Direction direction;
	private static Timer timer;

	public static void main(String[] args) {
		taskPerformer = (ActionEvent e) -> {
			model.movePaddle();
			buttonPressed = false;
		};
		timer = new Timer(TICKS_PER_SECOND, taskPerformer);
		timer.setInitialDelay(0);
	}

	public void directionInput(KeyEvent key) {
		int keyCode = key.getKeyCode();

		if (isNewGame) {
			isNewGame = false;
			model.chooseDifficulty();
			return;
		}

		if (isChoosingDifficulty) {
			switch (keyCode) {
			case KeyEvent.VK_1:
				setDifficulty(0);
				break;
			case KeyEvent.VK_2:
				setDifficulty(1);
				break;
			case KeyEvent.VK_3:
				setDifficulty(2);
				break;
			default:
				return;
			}
			isChoosingDifficulty = false;
			model.continueGame();
			timer.start();
			return;
		}

		if (isGameOver) {
			if (keyCode == KeyEvent.VK_Y) {
				model.chooseDifficulty();
			} else if (keyCode == KeyEvent.VK_N) {
				model.quit();
			} else {
				return;
			}
			isGameOver = false;
			return;

		}
		
		if (isGameWin) {
			if (keyCode == KeyEvent.VK_Y) {
				model.chooseDifficulty();
			} else if (keyCode == KeyEvent.VK_N) {
				model.quit();
			} else {
				return;
			}
			isGameOver = false;
			return;
		}

		switch (key.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (direction != Direction.RIGHT && !buttonPressed) {
                direction = Direction.LEFT;
            }
            buttonPressed = true;
            break;
		case KeyEvent.VK_RIGHT:
			 if (direction != Direction.LEFT && !buttonPressed) {
                 direction = Direction.RIGHT;
             }
             buttonPressed = true;
             break;
		case KeyEvent.VK_P:
			if (!isPaused) {
				isPaused = true;
				model.stopMusic();
				timer.stop();
			} else {
				isPaused = false;
				model.playMusic();
				timer.start();
			}
			break;
		default:
			break;
		}

		if (direction != null) {
			model.setDirection(direction);
		}
	}

	public void setGameOver(boolean isGameOver) {
		timer.stop();
		Controller.isGameOver = isGameOver;
	}
	
	public void setGameWin(boolean isGameWin) {
		timer.stop();
		Controller.isGameWin = isGameWin;
	}

	public void setNewGame(boolean isNewGame) {
		Controller.isNewGame = isNewGame;
	}

	public void setChoosingDifficulty(boolean isChoosingDifficulty) {
		timer.stop();
		this.isChoosingDifficulty = isChoosingDifficulty;
	}

	public void setDifficulty(int difficulty) {
		switch (difficulty) {
		case 0:
			TICKS_PER_SECOND = 1000 / 8;
			timer = new Timer(TICKS_PER_SECOND, taskPerformer);
			break;
		case 1:
			TICKS_PER_SECOND = 1000 / 15;
			timer = new Timer(TICKS_PER_SECOND, taskPerformer);
			break;
		case 2:
			TICKS_PER_SECOND = 1000 / 20;
			timer = new Timer(TICKS_PER_SECOND, taskPerformer);
			break;
		default:
			break;
		}
		model.setDifficulty(difficulty);
	}
}
