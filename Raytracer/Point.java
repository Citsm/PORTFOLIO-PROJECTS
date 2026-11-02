public class Point{
    private double x, y, z;
    
    public Point(double newX, double newY, double newZ){
        x = newX;
        y = newY;
        z = newZ;
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    
    public Point add(Vector v){
        double newX = x + v.getDX();
        double newY = y + v.getDY();
        double newZ = z + v.getDZ();
        return new Point(newX, newY, newZ);
    }
    
    public Vector subtract(Point p){
        double newX = x - p.getX();
        double newY = y - p.getY();
        double newZ = z - p.getZ();
        return new Vector(newX, newY, newZ);
    }
}