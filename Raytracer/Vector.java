public class Vector{
    private double dx, dy, dz;
    
    public Vector(double newDX, double newDY, double newDZ){
        dx = newDX;
        dy = newDY;
        dz = newDZ;
    }
    public double getDX(){ return dx; }
    public double getDY(){ return dy; }
    public double getDZ(){ return dz; }
    
    public Vector scale(double scalar){
        return new Vector(this.dx * scalar, this.dy * scalar, this.dz * scalar);
    }
    
    public Vector add(Vector v){
        double newDx = this.dx + v.getDX();
        double newDy = this.dy + v.getDY();
        double newDz = this.dz + v.getDZ();
        return new Vector(newDx, newDy, newDz);
    }
    
    public Vector subtract(Vector v){
        double newDx = this.dx - v.getDX();
        double newDy = this.dy - v.getDY();
        double newDz = this.dz - v.getDZ();
        return new Vector(newDx, newDy, newDz);
    }
    
    public double dot(Vector v){
        return (this.dx * v.getDX()) + (this.dy * v.getDY()) + (this.dz * v.getDZ());
    }
    
    public Vector cross(Vector v){
        double newDX = (this.dy * v.getDZ()) - (this.dz * v.getDY());
        double newDY = (this.dz * v.getDX()) - (this.dx * v.getDZ());
        double newDZ = (this.dx * v.getDY()) - (this.dy * v.getDX());
        return new Vector(newDX, newDY, newDZ);
    }

    public double length(){
        return Math.sqrt(this.dot(this));
    }
    
    public Vector normalize(){
        double L = this.length();
        return this.scale(1/L);
    }

}