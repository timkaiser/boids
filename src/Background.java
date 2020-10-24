import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
    BufferedImage img;

    double BLEND_FACTOR = 40;

    public Background(int width, int height, Obstacle[] obstacles){
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vec2 ij = new Vec2(i,j);
                double dist = 100000;

                if(obstacles != null) {
                    for (int o = 0; o < obstacles.length; o++) {
                        dist = smoothMin(dist, ij.dist(obstacles[o].pos) - obstacles[o].size / 2, BLEND_FACTOR);
                    }
                }

                if(dist > -3){
                    img.setRGB(i,j, new Color(219, 206, 186).getRGB());
                }else{
                    img.setRGB(i,j, new Color(76, 148, 129).getRGB());
                }
            }

            System.out.println((i*100)/width);
        }

    }


    double smoothMin(double distA, double distB, double k){
        double h = Math.max((k-Math.abs(distA-distB)),0.0) / k;
        return Math.min(distA, distB) - h*h*h*k*1.0/6.0;
    }
}
