package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGamePanel extends JPanel{
    static final int WIDTH = 320, HEIGHT = 320;
    static final int unit_size = 20;
    static final int DELAY = 250;
    boolean run = false;
    boolean spaceClick;
    private Timer timer;
    private String direction, nextDirection;
    private ArrayList<Sequence> snake = new ArrayList<>();
    private Sequence apple;
    int score; // num of apple that snake eaten; when we start a new game, score will automatically add one
    Random random;

    public SnakeGamePanel() {
        random = new Random();
        this.setPreferredSize( new Dimension(WIDTH, HEIGHT) );
		this.setBackground( Color.blue );
        this.setFocusable(true);
        newGame();
    }
    
    private class DirectionListener implements KeyListener {

        // To update the direction when we clicked the arrow keys in keyboard
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != "right") {
                        nextDirection = "left";
                    }
                    break;
                case  KeyEvent.VK_RIGHT:
                    if (direction != "left") {
                        nextDirection = "right";
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != "down") {
                        nextDirection = "up";
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != "up") {
                        nextDirection = "down";
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    spaceClick = true;
                    break;
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {} // do noting
        @Override
        public void keyReleased(KeyEvent e) {} // do nothing
    }

    private class MovingListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
            // copy the coordinate of the snake head (the last element in snake is the head of the snake)
            Sequence head = new Sequence(snake.get(snake.size()-1));

            // if snake eats the apple, update the snake and generate new apple
            if (snake.get(snake.size()-1).equals(apple)) {
                snake.add(apple);
                nextApple();
            }
            
            addKeyListener(new DirectionListener());

            if (direction == null) {
                direction = nextDirection; //initial the direction when new game started.
            } else {
                // when snake moving to the direction, we add the new position to the snake and delete the tail
                direction = nextDirection;
                switch (direction) {
                    case "left":
                        head.x -= unit_size;
                        snake.add( head );
                        snake.remove(0); // remove the tail of snake after a move made
                        break;
                    case "right":
                        head.x += unit_size;
                        snake.add( head );
                        snake.remove(0);
                        break;
                    case "up":
                        head.y -= unit_size;
                        snake.add( head );
                        snake.remove(0);
                        break;
                    case "down":
                        head.y += unit_size;
                        snake.add( head );
                        snake.remove(0);
                        break;
                }
            }

            if (!isGameOver()) {
                if (spaceClick) {
                    timer.stop();
                    int dg = JOptionPane.showConfirmDialog(null, "Pause, continue?", 
                    "waiting...", JOptionPane.DEFAULT_OPTION);
                    if (dg == JOptionPane.OK_OPTION) {
                        spaceClick = false;
                        timer.restart();
                    }
                }
                repaint(); // if game is not over, repaint
            } else {
                // if game over, stop the game and output dialog to ask if player want start new game
                timer.stop(); 
                int dialog = JOptionPane.showConfirmDialog(null, "Game Over, New Game?", 
                    "Warning type", JOptionPane.DEFAULT_OPTION);
                if (dialog == JOptionPane.OK_OPTION) {
                    newGame();
                }
            }
        }
    }

    @Override
    protected void paintComponent (Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        super.paintComponent(g);

        // draw apple
        g.setColor(Color.GREEN);
        g.fillRect(apple.x, apple.y, unit_size, unit_size);

        // draw snake
        for (int i=snake.size()-1; i >=0; i--) {
            g.setColor(Color.RED);
            g.fillRect(snake.get(i).x, snake.get(i).y, unit_size, unit_size);
        }
    }

    private void nextApple () {
        score++;
        
        while(true) {
            // random generate the coordinate for new apple
            int xval = random.nextInt( (int)(WIDTH-unit_size)/unit_size )*unit_size;
            int yval = random.nextInt( (int)(HEIGHT-unit_size)/unit_size )*unit_size;

            int count = snake.size();
            // check if the coordinate is on the body of the snake
            for (int i=0; i<snake.size(); i++) {
                if ( (xval == snake.get(i).x) && (yval == snake.get(i).y) ) {
                    count--;
                }
            }
            
            // if coordinate is not on the snake body, count equals to snake body size
            // generate new apple and break the while loop when count == snake.size()
            if (count == snake.size()) {
                apple = new Sequence(xval, yval);
                break;
            }
        }

        // update the speed level depends on the score
        if (score>0 && score%4==0) {
            if (timer.getDelay() > 50) {
                timer.setDelay(250 - score/4*20);
            }
        }
    }

    private boolean isGameOver () {
        //checks if head collides with body
		for(int i = 0; i < snake.size()-2; i++) {
			if( snake.get(i).equals( snake.get(snake.size()-1) ) ) {
				run = false;
                break;
			}
		}
        
        //check if head touches left border
		if(snake.get(snake.size() - 1).x < 0) {
			run = false;
		}
		//check if head touches right border
		if(snake.get(snake.size() - 1).x > WIDTH - unit_size) {
			run = false;
		}
		//check if head touches top border
		if(snake.get(snake.size() - 1).y < 0) {
			run = false;
		}
		//check if head touches bottom border
		if(snake.get(snake.size() - 1).y > HEIGHT - unit_size) {
			run = false;
		}

        // if game is running, isGameOver() should return false, true otherwise
        if (!run) {
            return true;
        } else {
            return false;
        }
    }

    private void newGame () {
        // clear the snake when a new game started
        if (snake.size() != 0) {
            snake.clear();
        }
        // clear direction and nextDirection if a new game started
        direction = null;
        nextDirection = null;
        spaceClick = false;
        
        Sequence temp = new Sequence(); // initial a cooridinate for the snake
        temp.x = random.nextInt( (WIDTH-unit_size)/unit_size )*unit_size;
        temp.y = random.nextInt( (WIDTH-unit_size)/unit_size )*unit_size;
        snake.add(temp);
        
        nextApple(); // initial a apple
		run = true; // initial run=true to make sure isGameOver==false
        score = -1; // reset score when new game started
		timer = new Timer(DELAY, new MovingListener());
		timer.start();
    }

    private class Sequence {
        private int x, y;

        public Sequence() {
            this.x = -1; 
            this.y = -1;
        }

        public Sequence(int a, int b) {
            this.x = a; 
            this.y = b;
        }

        public Sequence(Sequence c){
            x = c.x; y = c.y;
        }

        public boolean equals(Sequence object) {    
            if (this.x == object.x && this.y == object.y) {    
                return true;    
            } else {
                return false;
            }
        }
    }
}
