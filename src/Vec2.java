public class Vec2{
    double x, y;
    Vec2 xy = this;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }

    //vector math
    public Vec2 add(Vec2 v){ x += v.x;  y += v.y;  return this; }
    public Vec2 mul(Vec2 v){ x *= v.x;  y *= v.y;  return this; }
    public Vec2 sub(Vec2 v){ x -= v.x;  y -= v.y;  return this; }
    public Vec2 div(Vec2 v){ x /= v.x;  y /= v.y;  return this; }
    public Vec2 neg(){ x = -x; y = -y; return this; }


    public double dot(Vec2 v){ return x*v.x +y*v.y; }
    public double angle(Vec2 v){ return (length() == 0 | v.length() == 0) ? 0 : Math.acos(this.dot(v)/(this.length()*v.length())); }
    public double signed_angle(Vec2 v){ return Math.atan2(x*v.y-y*v.x,x*v.x+y*v.y); }
    public Vec2 rotate(double rad){
        double new_x = x * Math.cos(rad) - y * Math.sin(rad);
        double new_y = x * Math.sin(rad) + y * Math.cos(rad);
        x = new_x;
        y = new_y;
        return this;
    }

    //scalar math
    public Vec2 add(double d){ x += d;  y += d; return this; }
    public Vec2 mul(double d){ x *= d;  y *= d; return this; }
    public Vec2 sub(double d){ x -= d;  y -= d; return this; }
    public Vec2 div(double d){
        if(d==0){ y=x=0; System.err.println("ERROR: Divided by 0"); }
        x /= d;  y /= d; return this;
    }

    public Vec2 normalize(){ return length() == 0 ? this : this.div(this.length()); }
    public Vec2 norm(){ return normalize();}

    public double length(){ return Math.sqrt(x*x + y*y); }

    public double dist(Vec2 b){ return Vec.sub(b, this).length(); }

    public Vec2 copy(){ return new Vec2(x, y); }

    public String toString(){ return "("+x+", "+y+")"; }
}
