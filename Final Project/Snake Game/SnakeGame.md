# Snake Game

## Pengertian

Snake Game adalah sebuah game sederhana dimana pemain diberikan seekor ular dengan panjang yang bisa berubah-ubah tergantung objek yang ia makan. Pemain dapat mengontrol pergerakan ular dan memakan objek-objek yang telah disediakan (berupa buah) sambil mencegah benturan antara ular dengan dinding permainan atau tubuh ular itu sendiri.

## Kelas-Kelas

### Controller.java

Kelas ini merupakan kelas dimana semua input diterima kemudian diolah. Singkatnya, ini adalah kelas berisikan method-method yang membantu pemain untuk memainkan permainan.

Sebagai contoh 
switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN && !buttonPressed) {
                    direction = Direction.UP;
                }
                buttonPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP && !buttonPressed) {
                    direction = Direction.DOWN;
                }
                buttonPressed = true;
                break;
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

    baris kode di atas mengatur bagaimana ualr dapat digerakkan, yakni dengan arrow key dan beberapa tombol lain yang dapat ditekan untuk menjalankan fungsi lainnya seperti tombol 'P' untuk memberhentikan sementara permainan.

### Model.java

Kelas Model merupakan kelas dimana logika game berada. Pengecekan collision, generate objek yang kemudian akan dimakan oleh ular, sistem scoring, semua terletak di sini.

Modifikasi yang dilakukan terhadap kelas ini antara lain sebagai berikut

private int score = 0;
private int applesEaten = 0;
private int orangesEaten = 0;
private int rottenApplesEaten = 0;
private boolean isEatingApple;
private boolean isEatingOrange;
private boolean isEatingRottenApple;
private final Point apple = new Point();
private final Point orange = new Point();
private final Point rottenapple = new Point();

Kami membuat beberapa variabel baru untuk mendukung beberapa fitur yang hendak kami tambahkan. Varibel score contohnya, dibuat untuk menggantikan fungsi variabel applesEaten sehingga variasi score dari tiap objek yang dimakan dapat diberikan.

Di samping membuat variabel-variabel baru, kami tentunya membuat beberapa method, antara lain

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

untuk menggenerate jeruk,

else if (ateOrange()) {
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
            isEatingRottenApple = false;}

untuk mengecek apakah ular memakan jeruk (3 variabel boolen ditambahkan untuk mencetak string yang tepat sesuai dengan objek yang dimakan di sisi kanan layar permainan),

if(snakeBody.size() == 1) {
        	stopMusic();
            playGameOverSound();
            data[difficulty] = Math.max(score, data[difficulty]);
            view.update(difficulties[difficulty], score, data[difficulty]);
            direction = Direction.UP; // consider making this random
            view.gameOver();
        }

untuk mengecek apakah list ular masih memiliki isi atau tidak (baca: untuk memberhentikan game apabila ular memakan terlalu banyak rotten apple).

### Direction.java

Kelas Direction merupakan kelas untuk mengenkapsulasi arah gerak ular.

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;
}

### DifficultyPanel.java

Kelas DifficultyPanel merupakan kelas dimana tampilan layar saat pemain sedang memilih level yang ingin dimainkan dibuat.

Modifikasi yang kami lakukan terhadap kelas ini yakni pembuatan method baru sebagai berikut

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

 yang, seperti namanya, berfungsi untuk menggambar titik-titik pada tampilan layar.

### GameHeaderPanel.java

Kelas GameHeaderPanel merupakan kelas dimana tampilan header layar permainan dibuat. Modifikasi yang kami lakukan terhadap kelas ini yakni mengalihfungsikan fungsi awalnya yakni sebagai penampil score menjadi penampil pesan singkat. 

 public void paintHeader() {
        g2d.setColor(Color.white);
        Font font = new Font("Monospaced", Font.PLAIN, scale);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc,
                "Welcome to Snake Game!");
        g2d.drawGlyphVector(gv,
                width / 2 - ((int) gv.getVisualBounds().getWidth() / 2),
                height / 2 - ((int) gv.getVisualBounds().getHeight() / 2));
}

### GameOverPanel.java

Kelas GameOverPanel merupakan kelas dimana tampilan layar saat game selesai dibuat.

Modifikasi yang kami lakukan terhadap kelas ini adalah pembuatan method baru, yakni

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

yang, seperti namanya, berfungsi untuk menggambar titik-titik pada tampilan layar.

### GamePanel.java

Kelas GamePanel merupakan kelas dimana tampilan layar saat game sedang dimainkan dibuat. Modifikasi yang kami lakukan di kelas ini berhubungan erat dengan modifikasi yang telah dilakukan di kelas Model.java sebelumnya, di antaranya adalah penambahan variabel-variabel baru

private boolean isEatingApple;
private boolean isEatingOrange;
private boolean isEatingRottenApple;
private final Point apple;
private final Point orange;
private final Point rottenapple;

dan pembuatan method-method baru

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

method di atas menggambarkan tampilan game sekaligus memberikan paparan mengenai kontrol dan info-info yang perlu diketahui terkait game yang sedang dimainkan. Method di atas juga menampilkan progress dari permainan yang sedang berjalan.
        
### NewGamePanel.java
Kelas NewGamePanel merupakan kelas dimana tampilan layar saat game akan dimulai dibuat. Modifikasi yang kami lakukan di kelas ini antara lain

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

penambahan method di atas untuk menggambar titk-titik, dan perubahan terhadap jenis font yang kami gunakan

Font font = new Font("Monospaced", Font.BOLD, width / 10);

Font font = new Font("Arial", Font.HANGING_BASELINE, width / 30);

### View.java
Kelas view merupakan kelas dimana semua tampilan layar yang telah kita buat digabungkan menjadi satu.

Modifikasi yang kami lakukan pada kelas ini berkaitan erat dengan modifikasi di kelas-kelas sebelumnya khususnya yang berkaitan dengan parameter fungsi seperti ini,

public View(int width, int height, int scale, Deque<Point> snakeBody, Point apple, Point orange, Point rottenapple
    		,String difficulty, int score, int highScore, int applesEaten, int orangesEaten, int rottenApplesEaten, boolean isEatingApple, boolean isEatingOrange, boolean isEatingRottenApple)

dapat dilihat bahwa parameter menyimpan lebih banyak variable dibandingkan program awal.

Kami juga melakukan modifikasi pada method berikut

public void updateView(Deque<Point> snakeBody, Point apple, Point orange, Point rottenapple
    		,String difficulty, int score, int highScore, int applesEaten, int orangesEaten, int rottenApplesEaten, boolean isEatingApple, boolean isEatingOrange, boolean isEatingRottenApple) {
        gamePanel.setSnakeBody(snakeBody, apple, orange, rottenapple);
        gamePanel.update(difficulty, highScore, score, applesEaten, orangesEaten, rottenApplesEaten, isEatingApple, isEatingOrange, isEatingRottenApple);
        gamePanel.repaint();
}

### ViewListener.java
Kelas ViewListener merupakan kelas dimana tampilan yang telah kita buat menerima input dari luar, dalam hal ini keyboard, kemudian inputan itu akan disampaikan dan diolah oleh program. Karena pengolahan input sudah terlebih dahulu diatur dalam kelas Controller, yang perlu kita lakukan hanyalah membuat kelas Controller baru dan menggunakannya.

 Controller controller = new Controller();

## Dokumentasi


## Source

[https://github.com/jbberinger/Snake/tree/4271e93b399c01baa9e67da80383c220e16e691b]

### Kontributor
Nazhwa Ameera H.    05111940000133  (F)
Zahrotul Adillah    05111940000139  (A)