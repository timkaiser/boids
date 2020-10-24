import javax.swing.*;
import java.awt.*;

public class Main {
    //window size
    static int screen_width = 2000, screen_height = 1500;

    public Main(){
        //create window
        JFrame frame = new JFrame("Boids");
        frame.setSize(screen_width,screen_height+80);
        frame.setLayout(null);
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //create and add canvas for the game (which is also the main game instance
        frame.add(new GamePanel(screen_width, screen_height));

        frame.getContentPane().setPreferredSize(new Dimension(screen_width,screen_height));
        frame.pack();

        frame.setVisible(true);
    }

    public static void main(String args[]){
        new Main();
    }

}
