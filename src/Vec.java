public class Vec {
    //vector math
    public static Vec2 add(Vec2 v1, Vec2 v2){ return new Vec2(v1.x+v2.x, v1.y+v2.y); }
    public static Vec2 mul(Vec2 v1, Vec2 v2){ return new Vec2(v1.x*v2.x, v1.y*v2.y); }
    public static Vec2 sub(Vec2 v1, Vec2 v2){ return new Vec2(v1.x-v2.x, v1.y-v2.y); }
    public static Vec2 div(Vec2 v1, Vec2 v2){ return new Vec2(v1.x/v2.x, v1.y/v2.y); }
    public static double dot(Vec2 v1, Vec2 v2){ return v1.dot(v2); }
    public static double angle(Vec2 v1, Vec2 v2){ return v1.angle(v2); }
    public static Vec2 neg(Vec2 v){ return new Vec2(-v.x,-v.y); }

    public static double signed_angle(Vec2 v1, Vec2 v2){ return v1.signed_angle(v2); }
    public static Vec2 rotate(Vec2 v, double rad){ return v.copy().rotate(rad);  }

    //scalar math
    public static Vec2 add(Vec2 v, double d){ return new Vec2(v.x+d, v.y+d); }
    public static Vec2 mul(Vec2 v, double d){ return new Vec2(v.x*d, v.y*d); }
    public static Vec2 sub(Vec2 v, double d){ return new Vec2(v.x-d, v.y-d); }
    public static Vec2 div(Vec2 v, double d){ return new Vec2(v.x/d, v.y/d); }

    public static Vec2 add(double d, Vec2 v){ return add(v,d); }
    public static Vec2 mul(double d, Vec2 v){ return mul(v,d); }
    public static Vec2 sub(double d, Vec2 v){ return sub(v,d);}
    public static Vec2 div(double d, Vec2 v){ return div(v,d);}

    public static Vec2 pow2(Vec2 v){ return new Vec2(v.x*v.x, v.y*v.y); }
    
    public static double length(Vec2 v){ return v.length(); }

    public static Vec2 normalize(Vec2 v){ return v.length() == 0 ? v : div(v,v.length());}
    public static Vec2 norm(Vec2 v){ return normalize(v);}

    public static double dist(Vec2 v1, Vec2 v2){ return v1.dist(v2); }
}
