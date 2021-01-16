package model;

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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import util.Direction;
import view.View;

/**
 * Main model class as part of MVC design pattern.
 *
 * @author Justin Beringer
 */
public final class Model {

    private final int WIDTH = 1000;
    private final int HEIGHT = 600;
    private final int SCALE = 15;
    private final int GROWTH_SPURT = 8;

    private final int MAX_INDEX_X = (WIDTH - 400) / SCALE;
    private final int MAX_INDEX_Y = HEIGHT / SCALE;

    private int squaresToGrow;
    private int score = 0;
    private int applesEaten = 0;
    private int orangesEaten = 0;
    private int rottenApplesEaten = 0;
    private Direction direction = Direction.UP;
    private int difficulty;
    
    private boolean isEatingApple;
	private boolean isEatingOrange;
	private boolean isEatingRottenApple;
	
    private final View view;
    private final Point apple = new Point();
    private final Point orange = new Point();
    private final Point rottenapple = new Point();
    private final Random random = new Random();
    private final Deque<Point> snakeBody = new ArrayDeque<>();
    private final Set<Point> occupiedPositions = new LinkedHashSet();
    private Clip gameOverSound, eatAppleSound, gameMusicSound;

    private final String[] difficulties = {"Easy", "Medium", "Hard"};

    private final String HIGH_SCORE_EASY = "High Score " + difficulties[0] + ": ";
    private final String HIGH_SCORE_MEDIUM = "High Score " + difficulties[1] + ": ";
    private final String HIGH_SCORE_HARD = "High Score " + difficulties[2] + ": ";
    private final String GAMES_PLAYED = "Games Played: ";
    private final String SCORE = "Score: ";
    private final String APPLES_EATEN = "Apples Eaten: ";
    private final String ORANGES_EATEN = "Oranges Eaten: ";
    private final String ROTTENAPPLES_EATEN = "Bomb Eaten: ";

    private final String[] dataID = {HIGH_SCORE_EASY, HIGH_SCORE_MEDIUM,
                                     HIGH_SCORE_HARD, GAMES_PLAYED, SCORE, APPLES_EATEN, ORANGES_EATEN, ROTTENAPPLES_EATEN};

    private final int[] data = new int[dataID.length];

    private final int TOTAL_GAMES_PLAYED_LOC = 3;
    private final int TOTAL_APPLES_EATEN_LOC = 4;
    private final int TOTAL_ORANGES_EATEN_LOC = 5;
    private final int TOTAL_ROTTENAPPLES_EATEN_LOC = 6;
    private final int SCORE_LOC = 7;

    public Model() {
        loadData();
        occupiedPositions.add(apple);
        view = new View(WIDTH, HEIGHT, SCALE, snakeBody, apple, orange, rottenapple, difficulties[difficulty], score, Math.max(score, data[difficulty]), applesEaten, orangesEaten, rottenApplesEaten, isEatingApple, isEatingOrange, isEatingRottenApple);
        initSounds();
    }

    /**
     * Generates snake at center of grid moving in upward direction. Consider
     * initializing the snake position and direction randomly.
     */
    private void generateSnakeAtCenter() {
        int x = (WIDTH-400) / 2;
        int y = HEIGHT / 2;
        snakeBody.add(new Point(x, y));
        occupiedPositions.add(snakeBody.getFirst());
        squaresToGrow += GROWTH_SPURT;
    }

    /**
     * Generates an apple position randomly and avoids collisions.
     *
     * Originally tried to use HashSet.contains() to check for collisions, but
     * it wasn't catching collisions.
     */
    private void generateApple() {

        int x = 0;
        int y = 0;
        boolean spaceIsOccupied;
        do {
            spaceIsOccupied = false;
            x = random.nextInt((int) MAX_INDEX_X) * SCALE;
            y = random.nextInt((int) MAX_INDEX_Y) * SCALE;
            for (Point point : occupiedPositions) {
                if (point.getX() == x && point.getY() == y) {
                    System.out.println(":)");
                    spaceIsOccupied = true;
                }
            }
        } while (spaceIsOccupied);
        apple.setLocation(x, y);
    }
    
    private void generateOrange() {

        int x = 0;
        int y = 0;
        boolean spaceIsOccupied;
        do {
            spaceIsOccupied = false;
            x = random.nextInt((int) MAX_INDEX_X) * SCALE;
            y = random.nextInt((int) MAX_INDEX_Y) * SCALE;
            for (Point point : occupiedPositions) {
                if (point.getX() == x && point.getY() == y) {
                    System.out.println(":)");
                    spaceIsOccupied = true;
                }
            }
        } while (spaceIsOccupied);
        orange.setLocation(x, y);
    }

    private void generateRottenApple() {

        int x = 0;
        int y = 0;
        boolean spaceIsOccupied;
        do {
            spaceIsOccupied = false;
            x = random.nextInt((int) MAX_INDEX_X) * SCALE;
            y = random.nextInt((int) MAX_INDEX_Y) * SCALE;
            for (Point point : occupiedPositions) {
                if (point.getX() == x && point.getY() == y) {
                    System.out.println(":)");
                    spaceIsOccupied = true;
                }
            }
        } while (spaceIsOccupied);
        rottenapple.setLocation(x, y);
    }


    /**
     * Moves snake by moving tail position one grid square in front of the head
     * in the current direction. The translated tail is then dequeued before
     * being queued as the new head. If an apple is eaten, the snake is extended
     * by not dequeuing the tail.
     */
    public void moveSnake() {

        int nextHeadX = (int) snakeBody.getFirst().getX();
        int nextHeadY = (int) snakeBody.getFirst().getY();

        switch (direction) {
            case UP:
                nextHeadY -= SCALE;
                break;
            case DOWN:
                nextHeadY += SCALE;
                break;
            case LEFT:
                nextHeadX -= SCALE;
                break;
            case RIGHT:
                nextHeadX += SCALE;
                break;
            default:
                break;
        }

        if (collided(nextHeadX, nextHeadY)) {
            stopMusic();
            playGameOverSound();
            data[difficulty] = Math.max(score, data[difficulty]);
            view.update(difficulties[difficulty], score, data[difficulty]);
            direction = Direction.UP; 
            view.gameOver();
        }

        snakeBody.getLast().setLocation(nextHeadX, nextHeadY);
        if (ateApple()) {
            playEatAppleSound();
            snakeBody.addFirst(new Point(nextHeadX, nextHeadY));
            occupiedPositions.add(snakeBody.getFirst());
            generateApple();
            applesEaten++;
            score += 2;
            data[TOTAL_APPLES_EATEN_LOC]++;
            squaresToGrow += GROWTH_SPURT - 1;
            isEatingApple = true;
            isEatingOrange = false;
            isEatingRottenApple = false;
        } else if (ateOrange()) {
        	playEatAppleSound();
            snakeBody.addFirst(new Point(nextHeadX, nextHeadY));
            occupiedPositions.add(snakeBody.getFirst());
            generateOrange();
            orangesEaten++;
            score += 3;
            data[TOTAL_ORANGES_EATEN_LOC]++;
            squaresToGrow += GROWTH_SPURT - 1;
            isEatingApple = false;
            isEatingOrange = true;
            isEatingRottenApple = false;
        } else if (ateRottenApple()) {
        	playEatAppleSound();
            snakeBody.addFirst(new Point(nextHeadX, nextHeadY));
            occupiedPositions.add(snakeBody.getFirst());
            generateRottenApple();
            rottenApplesEaten++;
            score -= 1;
            data[TOTAL_ROTTENAPPLES_EATEN_LOC]++;
            squaresToGrow -= GROWTH_SPURT - 1;
            isEatingApple = false;
            isEatingOrange = false;
            isEatingRottenApple = true;
        }  else if (squaresToGrow > 0) {
            snakeBody.addFirst(new Point(nextHeadX, nextHeadY));
            occupiedPositions.add(snakeBody.getFirst());
            squaresToGrow--;
        }  else if (squaresToGrow < 0) {
        	snakeBody.removeLast();
        	occupiedPositions.remove(snakeBody.getFirst());
        	squaresToGrow++;
        } else {
            snakeBody.addFirst(snakeBody.removeLast());
        }
        
        if(snakeBody.size() == 1) {
        	stopMusic();
            playGameOverSound();
            data[difficulty] = Math.max(score, data[difficulty]);
            view.update(difficulties[difficulty], score, data[difficulty]);
            direction = Direction.UP; // consider making this random
            view.gameOver();
        }

        view.updateView(snakeBody, apple, orange, rottenapple, difficulties[difficulty], score, Math.max(score, data[difficulty]), applesEaten, orangesEaten, rottenApplesEaten, isEatingApple, isEatingOrange, isEatingRottenApple);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private boolean ateApple() {
        return snakeBody.getFirst().equals(apple);
    }
    
    private boolean ateOrange() {
    	return snakeBody.getFirst().equals(orange);
    }
    
    private boolean ateRottenApple() {
    	return snakeBody. getFirst().equals(rottenapple);
    }

    /**
     * Checks for snake collision with self and edges
     */
    private boolean collided(int nextHeadX, int nextHeadY) {
        boolean snakeCollision = snakeBody.contains(new Point(nextHeadX, nextHeadY));
        boolean leftEdgeCollision = nextHeadX < 0;
        boolean rightEdgeCollision = nextHeadX >= (WIDTH-400);
        boolean topEdgeCollision = nextHeadY < 0;
        boolean bottomEdgeCollision = nextHeadY >= HEIGHT;
        return snakeCollision
                || leftEdgeCollision
                || rightEdgeCollision
                || topEdgeCollision
                || bottomEdgeCollision;
    }

    public void newGame() {
        view.newGame();
    }

    public void continueGame() {
        clearModel();
        generateSnakeAtCenter();
        generateApple();
        generateOrange();
        generateRottenApple();
        playMusic();
        view.updateView(snakeBody, apple, orange, rottenapple, difficulties[difficulty], score, Math.max(score, data[difficulty]), applesEaten = 0, orangesEaten = 0, rottenApplesEaten = 0, isEatingApple, isEatingOrange, isEatingRottenApple);
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
        occupiedPositions.clear();
        snakeBody.clear();
        rottenapple.setLocation(0, 2);
        orange.setLocation(0, 1);
        apple.setLocation(0, 0);
        score = 0;
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
    
    /**
     * Loads game stats from text file. If no text file exists, a new one is
     * created.
     *
     */
    public void loadData() {
        Path path = Paths.get("./SnakeData.txt");
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

    /**
     * Saves game stats to text file. *
     */
    public void saveData() {
        for (int datum : data) {
            System.out.println(datum);
        }
        Path path = Paths.get("./SnakeData.txt");
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