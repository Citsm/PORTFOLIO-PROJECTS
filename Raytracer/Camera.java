public class Camera{
    private Point location;
    private Vector forward, up, right;
    private double xFoV, yFoV;
    
    public Camera(Point position, Vector forwardVector, Vector upVector, double fieldOfView, double aspectRatio){
        location = position;
        forward = forwardVector.normalize();
        up = upVector.normalize();
        right = forward.cross(up);
        xFoV = Math.toRadians(fieldOfView);
        yFoV = Math.atan(((Math.tan(xFoV)) / aspectRatio));
    }
    
    public Point imagePlanePoint(double u, double v){
        Vector term1 = right.scale(2 * (u-0.5) * Math.tan(xFoV));
        Vector term2 = up.scale(2 * (v-0.5) * Math.tan(yFoV));
        Point p = location.add(forward)
                          .add(term1)
                          .add(term2);
        return p;
    }
    
    public Ray generateRay(double u, double v){
        return new Ray(location, imagePlanePoint(u, v).subtract(location));
    }
}