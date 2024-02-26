import game.Game;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Chess");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(new Dimension(640, 640));
        frame.setResizable(false);

        new Game(frame);

        frame.pack();

        frame.setVisible(true);

    }

}