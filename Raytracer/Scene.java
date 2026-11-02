import java.util.ArrayList;
import java.util.stream.IntStream;

public class Scene{
    private Camera camera;
    private ArrayList<Surface> surfaces;
    private ArrayList<Light> lights;
    
    public Scene(Camera newCam){
        camera = newCam;
        surfaces = new ArrayList<>();
        lights  = new ArrayList<>();
    }
    
    public void setCamera(Camera newCam){
        camera = newCam;
    }

    public void addSurface(Surface s){
        surfaces.add(s);
    }
    
    public void addLight(Light li){
        lights.add(li);
    }
    
    //figures out what color a single ray sees 
    //render() calls it once for every sample ray. bouncesLeft is how many more mirror 
    //reflections its allowed to follow, bc a ray bouncing between two mirrors would 
    //otherwise recurse forever
    public Color computeVisibleColor(Ray r, int bouncesLeft){
        double closestDistance = Double.MAX_VALUE;
        Intersection closestIntersection = null;
        
        //keeps the intersection with the smallest distance (closest to the camera) 
        //every ray checks every surface, which is 
        //why scenes with a lot of triangles (like the grenade) are slow
        for(int i = 0;  i < surfaces.size(); i++){
            Intersection in = surfaces.get(i).intersect(r);
            if(in != null){
                double distance = in.getDistance();
                if(distance < closestDistance){
                    closestDistance = distance;
                    closestIntersection = in;
                }
            }
        }
        
        //base case, if the ray didnt hit anything it just sees the background, which is black
        if(closestIntersection == null){
            return new Color(0, 0, 0);
        }
        
        //black starts as no light and every light that reaches this point
        //adds its color to it (accurate naming is sort of like total running light)
        Color black = new Color(0, 0, 0);
        for(int i = 0; i < lights.size(); i++){
            //isShadowed shoots a ray from the hit point toward the light, if something
            //blocks it then that light doesnt contribute (shadows)
            if(!this.isShadowed(closestIntersection.getPosition(), lights.get(i))){  
                //materials determine how a light looks on a surface
                Color c = closestIntersection.getMaterial().computeLighting(closestIntersection, r, lights.get(i));
                black = black.tint(c);
            }
        }
        
        //base case, if the material isnt reflective (reflectiveness is 0) or the ray 
        //is out of bounces, lighting calculated above is the final color
        if(closestIntersection.getMaterial().getReflectiveness() == 0 || bouncesLeft == 0){
            return black;
        }else{
            //recursive case, shoot a new ray off the surface and see what it hits
            Vector n = closestIntersection.getNormal();
            Vector v = r.getDirection().scale(-1);
            
            //reflection formula is 2(n dot v)n - v, same idea as the 
            //mirror direction in phong
            Vector mirrorDir = n.scale(2 * n.dot(v)).subtract(v);
            Ray reflectedView = new Ray(closestIntersection.getPosition(), mirrorDir);
            
            //calls itself with one less bounce, reflected ray recurses
            Color reflectColor = computeVisibleColor(reflectedView, bouncesLeft - 1);
            
            //scale the reflected color by how reflective the material is
            reflectColor = reflectColor.scale(closestIntersection.getMaterial().getReflectiveness());
            black = black.tint(reflectColor);
            return black;
        }  
    }
    
    //time parameter not directly used in render(), see driver and scenecreator
    public ColorImage render(int xRes, int yRes, int numSamples, double time){
        ColorImage colorImage = new ColorImage(xRes, yRes);
        //numSamples is how many rays go through each pixel (anti-aliasing). sqrt of it gives
        //a grid, so 1024 samples is a 32 x 32 grid of rays inside every pixel
        int aaRes = (int) Math.sqrt(numSamples);
    
        //https://claude.ai/chat/df8bcc5e-db0d-4070-9cfc-3dac71813dba
        //https://www.youtube.com/watch?v=j2kp4d_axhI
        //https://www.geeksforgeeks.org/java/intstream-parallel-java/
        //https://www.youtube.com/watch?v=FWoYpM-E3EQ
        
        //my first version rendered every pixel of the image one at a time on ONE of the laptops cores
        //(individual processing units of the cpu)
        //what parallel processing does is split that action up across cores which speeds up rendering
        //this is good for the raytracer bc every pixel is completely independent of one another
        //an intstream is a stream of primitive ints (built into the class not objects)
        
        //streams are sequences of elements that allow you to perform functions on
        //data collections (like list) in a declarative way
        //that can bypass the memory usage of turning int values into Integer objects
        
        //this first line essentially says: for every int in the range (0, xRes), do this
        //.parallel() is the method that tells the computer to split that work across all cores
        
        //so across all cores, for every int in the range(0, xRes), do this:
        IntStream.range(0, xRes).parallel().forEach(x ->{
            for(int y = 0; y < yRes; y++){
                Color c = new Color(0, 0, 0);
    
                for(int i = 0; i < aaRes; i++){
                    for(int j = 0; j < aaRes; j++){
                        //x + (i + 0.5) / aaRes is the center of grid cell i inside pixel x
                        double u = (x + (i + 0.5) / aaRes) / xRes;
                        double v = (y + (j + 0.5) / aaRes) / yRes;
    
                        Ray ray = camera.generateRay(u, v);
                        c = c.add(computeVisibleColor(ray, 3));
                    }
                }
    
                //average all samples to get pixels final color, smooths edges
                c = c.scale(1 / (Math.pow(aaRes, 2)));
                //threads only write their own column of pixels so they dont interfere w each other
                colorImage.setColor(x, y, c);
            }
        });
    
        return colorImage;
    }
    
    //checks if anything is blocking light from reaching point p
    //returns true if p is in shadow from this light, which makes 
    //computeVisibleColor skip that lights contribution
    public boolean isShadowed(Point p, Light li){
        Ray shadowRay = new Ray(p, li.computeLightDirection(p));
        
        for(int i = 0; i < surfaces.size(); i++){
            Intersection in = surfaces.get(i).intersect(shadowRay);
            if(in != null && in.getDistance() < li.computeLightDistance(p)){
                return true;
            }
        }
        
        return false;
    }
}