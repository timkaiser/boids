import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class GamePanel extends JPanel implements Runnable {
    static int width, height;

    static boolean ENABLE_BACKGROUND = false;
    BufferedImage background;

    Boid[] boids;
    int number_of_boids = 1000;

    static Obstacle[] obstacles;
    int number_of_obstacles = 80;

    boolean running = false;

    public GamePanel(int width, int height){
        super();

        this.width = width;
        this.height = height;

        setSize(this.width, this.height);
        setBackground(new Color(237, 232, 221));
        setLayout(null);

        setup();

        running = true;
        new Thread(this).start();
    }

    void setup(){
        boids = new Boid[number_of_boids];
        for(int i = 0; i<boids.length; i++){ boids[i] = new Boid(); }

        obstacles = new Obstacle[number_of_obstacles];
        for(int i = 0; i<obstacles.length; i++){ obstacles[i] = new Obstacle(); }


        if(ENABLE_BACKGROUND) background = new Background(width, height, obstacles).img;

        //boids[0].debug = true;
    }

    public void run(){
        long frame_time_old = new Date().getTime();
        while(true) {
            long frame_time_new = new Date().getTime();
            double deltaTime = (frame_time_new-frame_time_old)/1000.0;
            frame_time_old = frame_time_new;

            //neighbors
            calcNeighbors();

            //move
            for(Boid b : boids){ b.move(deltaTime); }

            //render
            repaint();

            try{Thread.sleep(10);}catch(Exception e){};
        }
    }

    void calcNeighbors(){
        for(Boid b : boids){ b.resetNeighbors(); }

        for (int i = 0; i < boids.length; i++) {
            for (int j = i; j < boids.length; j++) {
                //check if in distance
                if(Vec.dist(boids[i].pos, boids[j].pos) < Boid.view_range){
                    //checkif in angle
                    Vec2 vec_ij = Vec.sub(boids[j].pos, boids[i].pos);
                    if(Vec.angle(boids[i].dir, vec_ij) < Boid.view_angle){   boids[i].addNeighbor(boids[j]);  }
                    if(Vec.angle(boids[i].dir, vec_ij.neg()) < Boid.view_angle){  boids[j].addNeighbor(boids[i]);  }
                }
            }
        }
    }

    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        if(ENABLE_BACKGROUND) g.drawImage(background, 0, 0, this);

        if(!running){ return; }

        for(Boid b : boids){ b.draw(g); }
        if(!ENABLE_BACKGROUND) for(Obstacle o : obstacles){ o.draw(g); }
    }

}
