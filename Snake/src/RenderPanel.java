import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class RenderPanel extends JPanel implements ActionListener, KeyListener {


    public Toolkit toolkit;

    public RenderPanel renderPanel;

    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    public int score = 0;

    public double speed = 0, direction = DOWN, tailLength = 5;

    public double time;

    public static Snake snake;

    public Timer timer = new Timer(20, this);

    public Point head;

    public Point apple;

    public ArrayList<Point> body = new ArrayList<>();

    private boolean running = true;

    int appleCount = 2;

    public static int level = 1;

    Random random = new Random();

    public int moveSpeed = 4;

    public boolean over, paused;

    MP3Player mp3Player = new MP3Player();


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;


        ImageIcon ic = new ImageIcon("Snake\\Snake.png");
        g2d.drawImage(ic.getImage(), 0, 0, null);

//      Drawing the Background
        g.setColor(Color.black);
        g.fillRect(642, 0, 100, 480);

//      Drawing the Apple
        g.setColor(Color.RED);
        g.fillRect(snake.apple.x, snake.apple.y, 12, 12);

//      Drawing the Head
        g.setColor(Color.WHITE);
        g.fillRect(snake.head.x, snake.head.y, 9, 9);

//      Drawing the Body
        g.setColor(Color.GREEN);
        for (Point point : snake.body) {
            g.fillRect(point.x, point.y, 7, 7);
        }
//        ScoreCounter
        // g.setFont();
        g.drawString("LEVEL", 660, 30);
        g.drawString(Integer.toString(snake.level), 660, 60);
        g.drawString("SCORE", 660, 90);
        g.drawString(Integer.toString(snake.score), 660, 120);
        g.drawString("PRESS SPACE", 648, 430);
        g.drawString("TO PAUSE", 660, 460);
        g.setColor(Color.WHITE);
        g.drawString("TIME : ",660,210);
        g.drawString(Double.toString(snake.time/50),660,240);


    }


    public boolean testCollision(int x, int y) {
        if (body.size() > 1) {
            for (Point point : body) {
                if (point == new Point(x, y)) {
                    return false;
                }
            }
        }
        return true;


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        renderPanel.repaint();
        speed++;
        running = true;

        if (speed % 0.5 == 0 && head != null && !over && !paused) {

            time++;

            body.add(new Point(head.x, head.y));


            if (direction == UP) {
                if (head.y - 1 >= 0 && testCollision(head.x, head.y - 1)) {
                    head = new Point(head.x, head.y - moveSpeed);
                } else {
                    running = false;

                }
            }

            if (direction == DOWN) {
                if (head.y + 1 < 470 && testCollision(head.x, head.y + 1)) {
                    head = new Point(head.x, head.y + moveSpeed);
                } else {
                    running = false;
                }
            }

            if (direction == LEFT) {
                if (head.x - 1 >= 0 && testCollision(head.x - 1, head.y)) {
                    head = new Point(head.x - moveSpeed, head.y);
                } else {
                    running = false;
                }
            }

            if (direction == RIGHT) {
                if (head.x + 1 < 630 && testCollision(head.x + 1, head.y)) {
                    head = new Point(head.x + moveSpeed, head.y);
                } else {
                    running = false;
                }
            }
        }

        if ((head.x < apple.x + 9 && head.x > apple.x - 9) && (head.y < apple.y + 9 && head.y > apple.y - 9)) {
            tailLength += 35;
            snake.score++;
            for (Point point : body) {
                if (!apple.equals(point))
                    mp3Player.play("Snake\\apple.wav");
                    apple.move(random.nextInt(600), random.nextInt(440));
            }
        }


        for (Point point : body) {
            if (head.x == point.x && head.y == point.y)
                running = false;

        }

        if (body.size() > tailLength) {
            body.remove(0);


        }

        checkEnd();
        gameOver();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int i = e.getKeyCode();

        if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT) {
            direction = LEFT;
        }

        if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT) {
            direction = RIGHT;
        }

        if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN) {
            direction = UP;
        }

        if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP) {
            direction = DOWN;
        }
        if (i == KeyEvent.VK_SPACE) {
            if (paused) {
                mp3Player.stopAll();
                mp3Player.play("Snake\\GameRunning.mp3",true);
                paused = !paused;
            } else {
                if (over) startGame();
                else {
                    mp3Player.stopAll();
                    mp3Player.play("Snake\\Paused.mp3");
                    paused = !paused;
                }
            }

        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void startGame() {

        paused = false;
        over = false;
        appleCount = level * 5;
        time = 0;
        score = 0;
        mp3Player.play("Snake\\GameRunning.mp3", true);
        head = new Point(320, 240);
        body = new ArrayList<Point>();
        tailLength = 0;
        speed++;
        for (int i = 0; i < appleCount; i++) {
            apple = new Point(random.nextInt(600), random.nextInt(440));
        }
        timer.start();


    }

    public void checkEnd() {

        if (score == appleCount) {
            level++;
            moveSpeed += 2;
            JOptionPane.showMessageDialog(null, "Good work you have completed the level " + (level - 1) + " lets move to the next one!");
            startGame();
        }

    }

    public void gameOver() {
        if (!running) {

            mp3Player.stopAll();
            mp3Player.play("Snake\\Lost.mp3");
            JOptionPane.showMessageDialog(null, "GAME OVER" + "\nLevel : \t" + snake.level + "\nTime : \t" + snake.time/50 + "\nScore : \t" + snake.score );
            System.exit(0);
        }
    }
}
