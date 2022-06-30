package Snake;

import javax.swing.*;
import java.awt.*;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakeGamePanel panel = new SnakeGamePanel();
		frame.getContentPane().add(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);	
    }
}
