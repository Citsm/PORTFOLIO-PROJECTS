import java.io.*;
import java.util.*;
/**
 * Details the static methods to creating various scenes for use in the raytracer.
 * scene1() is included as an example. You can add more static methods (for example scene2(),
 * scene3(), etc.) to create different scenes without affecting scene1.
 *
 * @author Ben Farrar
 * @version 2019.05.22
 */
public class SceneCreator {
    /*public static Scene scene1(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,0,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                20,                     // field of view
                                xResolution/yResolution ); // aspect ratio
        Scene s = new Scene(cam);
        
        //Each sphere takes a Point (its center), the radius, and a material.
        //For now, since we have not implemented the Material classes, we simply say they are null.
        Surface s1 = new Sphere(new Point(0,0,-20),3, null);
        s.addSurface(s1);
        Surface s2 = new Sphere(new Point(0,4,-15),1, null);
        s.addSurface(s2);
        Surface s3 = new Sphere(new Point(5,0,-20),1.5, null);
        s.addSurface(s3);
        //Each triangle takes 3 Points (its vertexes), and a material.
        Surface t1 = new Triangle(new Point(-3.5,-1,-15), new Point(-3.5,1,-15), new Point(-5,0,-16), null);
        s.addSurface(t1);
        Surface floor = new Triangle(new Point(0,-5,0), new Point(3000,-5,-1000), new Point(-3000,-5,-1000), null);
        s.addSurface(floor);
        
        return s;
    }*/
    
    public static Scene scene2(double xResolution, double yResolution){
        Camera cam = new Camera(new Point(0,0,0),       // camera location
                                new Vector(0,0,-1),     // forward vector/view direction
                                new Vector(0,1,0),      // up vector
                                20,                     // field of view
                                xResolution/yResolution ); // aspect ratio
        Scene s = new Scene(cam);
        
        Material redMat = new MirrorPhong(new Color(1, 0, 0), new Color(1, 1, 1), 10, 0.5);
        Material blueMat =  new Phong(new Color(0, 0, 1), new Color(1, 1, 1), 10);
        Material greenMat = new Lambert(new Color(0, 1, 0));
        Material greyMat = new Lambert(new Color(0.5, 0.5, 0.5));
        
        s.addSurface(new Cone(new Point(0.55, 0, -40), new Point(0.55, 5, -40), 3, redMat));
        s.addSurface(new Sphere(new Point(-7, 0, -40), 3, greenMat));
        s.addSurface(new Tube(new Point(7, -3, -40), new Point(7, 3, -40), 2, blueMat));
        
        Light light1 = new PointLight(new Color(1, 1, 3), new Point(0, 10, -20));
        s.addLight(light1);
        
        Surface floor = new Triangle(new Point(-1000, -5, -1000), new Point(1000, -5, -1000), new Point(0, -5, 1000), greyMat);
        s.addSurface(floor);
        
        return s;
    }
    
    public static Scene scene3(double xResolution, double yResolution, double time)throws IOException{
        double angle = time * 2 * Math.PI * 3; // 3 full 360 rotationa over all frames
        double radius = 15;
        double camX = Math.sin(angle) * radius; //rads
        double camZ = Math.cos(angle) * radius;
    
        Camera cam = new Camera(
            new Point(camX, 1, camZ),
            new Vector(-camX, 0.3, -camZ),
            new Vector(0, 1, 0),
            20,
            xResolution / yResolution
        );
        Scene s = new Scene(cam);
    
        Phong redShinyMat = new Phong(new Color(1, 0, 0), new Color(1, 1, 1), 10);
        Lambert greyMat = new Lambert(new Color(0.5, 0.5, 0.5));
        Light light1 = new PointLight(new Color(1,1,1), new Point(5, 5, 5));
        s.addLight(light1);
        
        Surface floor = new Triangle(new Point(-1000, 0, -1000), new Point(1000, 0, -1000), new Point(0, 0, 1000), greyMat);
        s.addSurface(floor);
        
        List<Surface> meshTriangles = ObjParser.parse("Car.obj", redShinyMat);
        System.out.println("Parsed " + meshTriangles.size() + " triangles");
        for(int i = 0; i < meshTriangles.size(); i++){
            s.addSurface(meshTriangles.get(i));
        }    
        
        return s;
    }
    
    public static Scene scene4(double xResolution, double yResolution) throws IOException{
        Camera cam = new Camera(
            new Point(-0.4, 0.5, 2.3),
            new Vector(0.18, -0.1, -1),
            new Vector(0, 1, 0),
            20,
            xResolution / yResolution
        );
        Scene s = new Scene(cam);
    
        Phong greenMat = new Phong(new Color(0.1, 0.35, 0.1), new Color(0.2, 0.4, 0.2), 20);
        Lambert brownMat = new Lambert(new Color(0.6, 0.3, 0.1));
        
        Light light1 = new PointLight(new Color(1,1,1), new Point(5, 5, 5));
        s.addLight(light1);
        Light light2 = new PointLight(new Color(1, 0.4, 0.0), new Point(0.5, 0.1, 0.3));
        s.addLight(light2);
        
        Surface floor = new Triangle(new Point(-1000, 0, -1000), new Point(1000, 0, -1000), new Point(0, 0, 1000), brownMat);
        s.addSurface(floor);
        
        List<Surface> meshTriangles = ObjParser.parse("grenade.obj", greenMat);
        System.out.println("Parsed " + meshTriangles.size() + " triangles");
        for(int i = 0; i < meshTriangles.size(); i++){
            s.addSurface(meshTriangles.get(i));
        }    
        
        return s;
    }
}
