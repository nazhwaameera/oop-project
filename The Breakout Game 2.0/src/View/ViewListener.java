package View;

import Controller.Controller;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ViewListener implements KeyListener {
	
	boolean isGameOver;
	boolean isGameWin;
    Controller controller = new Controller();

    public ViewListener() {
        isGameOver = false;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        //System.out.println(key.toString());
    	controller.directionInput(key);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void setGameOver(boolean isGameOver) {
        controller.setGameOver(isGameOver);
    }
    
    public void setGameWin(boolean isGameWin) {
        controller.setGameWin(isGameWin);
    }

    public void setNewGame(boolean isNewGame) {
        controller.setNewGame(isNewGame);
    }
    
    public void setChoosingDifficulty(boolean isChoosingDifficulty) {
        controller.setChoosingDifficulty(isChoosingDifficulty);
    }
}
