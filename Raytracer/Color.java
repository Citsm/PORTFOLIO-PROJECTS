public class Color{
    private double r, g, b;
    
    public Color(double newR, double newG, double newB){
        r = newR;
        g = newG;
        b = newB;
    }

    public double getR(){return r;}
    public double getG(){return g;}
    public double getB(){return b;}

    public Color add(Color c){
        double newR = r + c.getR();
        double newG = g + c.getG();
        double newB = b + c.getB();
        return new Color(newR, newG, newB);
    }
    
    public Color scale(double scalar){
        return new Color(r * scalar, g * scalar, b * scalar);
    }
    
    public int toARGB() {
        int ir = (int)(Math.min(Math.max(r, 0), 1) * 255 + 0.1);
        int ig = (int)(Math.min(Math.max(g, 0), 1) * 255 + 0.1);
        int ib = (int)(Math.min(Math.max(b, 0), 1) * 255 + 0.1);
        return (ir << 16) | (ig << 8) | (ib << 0);
    }
    
    public Color shade(Color c){
        double newR = this.getR() * c.getR();
        double newG = this.getG() * c.getG();
        double newB = this.getB() * c.getB();
        return new Color(newR, newG, newB);
    }
    
    public Color tint(Color c){
        double newR = this.getR() + (1 - this.getR()) * c.getR();
        double newG = this.getG() + (1 - this.getG()) * c.getG();
        double newB = this.getB() + (1 - this.getB()) * c.getB();
        return new Color(newR, newG, newB);
    }

}