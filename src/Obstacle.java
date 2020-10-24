import java.awt.*;

public class Obstacle {
    double size = Boid.size;

    Vec2 pos;

    Color color = new Color(36, 116, 64);

    public Obstacle(){
        pos = new Vec2(Math.random()*GamePanel.width, Math.random()*GamePanel.height);
        size = Math.random()*size*3+size*2;
    }

    void draw(Graphics g){
        g.setColor(color);
        g.fillOval((int)(pos.x-size/2), (int)(pos.y-size/2), (int)size , (int)size);
    }
}
