import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

public class Boid {
    // common stats
    static double size = 20;
    static double view_range = size*2.5;
    static double personal_space = size*2.5;
    static double obstacle_avoidance_space = size*1/2;
    static double view_angle = 0.75*Math.PI;
    static double steering_angle = 0.05*Math.PI;

    //personal stats
    Vec2 pos;
    Vec2 dir;
    double speed = size*7;
    double speed_variation = size*1;

    //weights
    double w_stay = 4;
    double w_follow = 1;
    double w_avoidB = 1;
    double w_avoidO = 10;
    double w_group = 3;

    double noise = 0.5;



    List<Boid> neighbors;

    //appearance
    //color
    Color color;
    double color_variation = 10;
    double[] rand_col;

    //polygon vertices
    Vec2[] vertices = {new Vec2(0,size*2/3), new Vec2(size/3,-size/3), new Vec2(-size/3,-size/3)};

    //debug appearance
    boolean debug = false;


    //constructor
    public Boid(){
        initialize();
    }

    void initialize(){
        //set position and direection
        pos = new Vec2(Math.random()*GamePanel.width, Math.random()*GamePanel.height);
        dir = new Vec2(Math.random()-0.5,Math.random()-0.5).normalize().mul(speed);

        //set speed
        speed = Math.random()*speed_variation + speed;

        //set color and color variation array
        color = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
        rand_col = new double[3];

        //initialize neighbor list
        resetNeighbors();
    }

    void resetNeighbors(){ neighbors = new ArrayList<Boid>(); }
    void addNeighbor(Boid b){ neighbors.add(b); }


    void move(double deltaTime){
        //Calculate new direction
        Vec2 new_dir = dir.normalize().mul(w_stay)
                .add(avoidBoidDirection().mul(w_avoidB))        //avoid other boids
                .add(avoidObstacleDirection().mul(w_avoidO))    //avoid obstacles
                .add(followDirection().mul(w_follow))           //follow other boids
                .add(groupCenter().mul(w_group));               //group with other boids

        double angle = dir.signed_angle(new_dir);

        dir.rotate(Math.signum(angle)*Math.min(angle,(steering_angle+(Math.random()-0.5)*deltaTime))*noise);

        dir.normalize().mul(speed);

        //move boid
        pos.add(Vec.mul(dir,deltaTime));

        /* screen wrap
        if(pos.x < -size*2){ pos.x = GamePanel.width + size; }
        if(pos.y < -size*2){ pos.y = GamePanel.height + size; }
        if(pos.x > GamePanel.width + size*2){ pos.x = 0 - size; }
        if(pos.y > GamePanel.height + size*2){ pos.x = 0 - size; }
        */

        calcColor();
    }

    void calcColor(){
        //set color
        double[] rand_col_n = {rand_col[0], rand_col[1], rand_col[2]};
        double weight_own = 10;
        double[] rgb = {color.getRed()*weight_own, color.getGreen()*weight_own, color.getBlue()*weight_own};

        for(Boid n : neighbors){
            rgb[0] += n.color.getRed();
            rgb[1] += n.color.getGreen();
            rgb[2] += n.color.getBlue();
            rand_col_n[0] += n.rand_col[0];
            rand_col_n[1] += n.rand_col[1];
            rand_col_n[2] += n.rand_col[2];
        }

        for(int i = 0; i<3; i++) {
            rgb[i] /= (neighbors.size() + weight_own);
            rand_col_n[i] /= neighbors.size() + 1;
            double var_factor = color_variation + color_variation * neighbors.size();
            rgb[i] += rand_col_n[i] * var_factor;//(Math.random()-0.5)*2*var_factor;
            rgb[i] = Math.max(0, Math.min(rgb[i], 255));

            rand_col[i] = (Math.random() - 0.5) * 2;
        }
        color = new Color((int)rgb[0],(int)rgb[1],(int)rgb[2]);
    }

    Vec2 followDirection(){
        Vec2 follow_dir = dir.copy();
        for(Boid b : neighbors){ follow_dir.add(b.dir);   }
        return follow_dir.div(neighbors.size()+1).normalize();
    }

    Vec2 groupCenter(){
        Vec2 group_center = pos.copy();
        for(Boid b : neighbors){ group_center.add(b.pos);   }
        return group_center.div(neighbors.size()+1).sub(pos).normalize();
    }

    Vec2 avoidBoidDirection(){
        Vec2 avoid_dir = new Vec2(0,0);
        for(Boid b : neighbors){
            double dist = pos.dist(b.pos);
            if(dist <= personal_space && dist !=0){
                avoid_dir.add(Vec.sub(pos,b.pos).normalize().mul(personal_space/dist));
            }
        }

        return avoid_dir;
    }

    Vec2 avoidObstacleDirection(){
        Vec2 avoid_dir = new Vec2(0,0);

        //avoid sides
        if(pos.x != 0 && pos.x < obstacle_avoidance_space){  avoid_dir.add(new Vec2(obstacle_avoidance_space/pos.x,0)); }
        if(pos.y != 0 && pos.y < obstacle_avoidance_space){  avoid_dir.add(new Vec2(0, obstacle_avoidance_space/pos.y));}
        if(pos.x != GamePanel.width && pos.x > GamePanel.width - obstacle_avoidance_space){  avoid_dir.add(new Vec2(obstacle_avoidance_space/(pos.x-GamePanel.width),0)); }
        if(pos.y != GamePanel.height && pos.y > GamePanel.height - obstacle_avoidance_space){  avoid_dir.add(new Vec2(0, obstacle_avoidance_space/(pos.y-GamePanel.height)));}

        //avoid obstacles
        for(Obstacle o : GamePanel.obstacles){
            if(pos.dist(o.pos) <= obstacle_avoidance_space+o.size) avoid_dir.add(Vec.sub(pos,o.pos).normalize().mul(obstacle_avoidance_space/pos.dist(o.pos)));
        }

        return avoid_dir;
    }

    void draw(Graphics g){
        if(debug) drawDebug(g);

        double angle = new Vec2(0,1).signed_angle(dir);
        Vec2[] global_vertices = {Vec.rotate(vertices[0],angle).add(pos),Vec.rotate(vertices[1],angle).add(pos),Vec.rotate(vertices[2],angle).add(pos)};
        int[] vertices_x = {(int)global_vertices[0].x, (int)global_vertices[1].x, (int)global_vertices[2].x};
        int[] vertices_y = {(int)global_vertices[0].y, (int)global_vertices[1].y, (int)global_vertices[2].y};

        g.setColor(color);
        g.fillPolygon(vertices_x,vertices_y,3);

    }

    void drawDebug(Graphics g){
        g.setColor(Color.gray);
        int angle = (int)((dir.angle(new Vec2(1,0))* (dir.y <0 ? 1 : -1)-view_angle)*180.0/Math.PI);
        g.fillArc((int)(pos.x-view_range), (int)(pos.y-view_range), (int)view_range*2, (int)view_range*2, angle, (int)(view_angle * 180/Math.PI * 2));

        g.setColor(Color.cyan);
        g.drawLine((int)pos.x, (int)pos.y, (int)(pos.x+Vec.norm(dir).x*view_range), (int)(pos.y+Vec.norm(dir).y*view_range));

        g.setColor(Color.RED);
        for(Boid b : neighbors) {
            g.drawLine((int)pos.x, (int)pos.y, (int)b.pos.x, (int)b.pos.y);
        }
    }
}
