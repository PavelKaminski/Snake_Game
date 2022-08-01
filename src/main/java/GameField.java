import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private final int APPLE_AMOUNT = 3;

    private Image dot;
    private Image apple;

    private int[] coorX = new int[ALL_DOTS];
    private int[] coorY = new int[ALL_DOTS];

    private int[] appleX = new int[3];
    private int[] appleY = new int[3];

    private int dots;
    private Timer timer;
    private int delayTime = 150;
    private boolean inGame = true;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    public GameField() {
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void loadImage() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    public void createApple() {
        Random random = new Random();
        for (int i = 0; i < APPLE_AMOUNT; i++) {
            appleX[i] = random.nextInt(19) * DOT_SIZE;
            appleY[i] = random.nextInt(19) * DOT_SIZE;
        }
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            coorX[i] = 48 - i * DOT_SIZE;
            coorY[i] = 48;
        }

        timer = new Timer(delayTime, this);
        timer.start();

        createApple();
    }

    public void checkApple() {
        for (int i = 0; i < APPLE_AMOUNT; i++) {
            if (coorX[0] == appleX[i] && coorY[0] == appleY[i]) {
                dots++;
                createApple();
                if ((dots - 3) % 5 == 0) {
                    delayTime -= 20;
                    timer.setDelay(delayTime);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            for (int i = 0; i < APPLE_AMOUNT; i++) {
                g.drawImage(apple, appleX[i], appleY[i], this);
            }
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, coorX[i], coorY[i], this);
            }
        } else {
            String str = "Game over";
            g.setColor(Color.CYAN);
            g.drawString(str, SIZE / 6, SIZE / 2);
        }
    }

    public void checkCollision() {
        for (int i = 1; i < dots; i++) {
            if (coorX[0] == coorX[i] && coorY[0] == coorY[i]) {
                inGame = false;
            }
        }
        if (coorX[0] > SIZE) {
            coorX[0] = 0;
        }
        if (coorX[0] < 0) {
            coorX[0] = SIZE;
        }
        if (coorY[0] > SIZE || coorY[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            coorX[i] = coorX[i - 1];
            coorY[i] = coorY[i - 1];
        }
        if (left) {
            coorX[0] -= DOT_SIZE;
        }
        if (right) {
            coorX[0] += DOT_SIZE;
        }
        if (up) {
            coorY[0] -= DOT_SIZE;
        }
        if (down) {
            coorY[0] += DOT_SIZE;
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent key) {
            super.keyPressed(key);
            int keyKode = key.getKeyCode();

            if (keyKode == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (keyKode == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (keyKode == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (keyKode == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
        }
    }

}
