import java.awt.image.BufferedImage;
//https://www.tutorialspoint.com/java_dip/java_buffered_image.htm
//a bufferedimage is a representation of images in memory
//they allow direct access to pixel data and color information
//its basically colorimage but already built into java so we need to convert
//our colorimage to a bufferedimage
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;
import java.util.*;
import org.jcodec.api.awt.AWTSequenceEncoder; //i heard about this library from greg, he used it for videos before me

/**
 * Driver for the various stages of the image generation and saving process.
 * 
 * @author Ben Farrar
 * @version 2019.05.22
 */
public class RaytracerDriver {
    public static void main(){
        //Size of the final image. This will DRAMATICALLY affect the runtime.
        int shapesXResolution = 800;
        int shapesYResolution = 600;
        
        int grenadeXResolution = 1920;
        int grenadeYResolution = 1080;
        
        int carXResolution = 1280;
        int carYResolution = 720;
        int frames = 120;
        
        List<BufferedImage> carFrames = new ArrayList<BufferedImage>();
        
        //Create the scene. You can change this when you make other scene creation methods to select
        //which scene to render.
        System.out.println("Creating scene...");
        
        Scene scene2 = SceneCreator.scene2(shapesXResolution, shapesYResolution);
        Scene scene4 = null;
        try{
            scene4 = SceneCreator.scene4(grenadeXResolution, grenadeYResolution);
        }catch(IOException e){
            System.out.println("Problem loading OBJ: " + e.getMessage());
        }
        
        //Render the scene into a ColorImage
        System.out.println("Rendering image...");
        ColorImage shapesImage = scene2.render(shapesXResolution,shapesYResolution, 16, 1);
        if(scene4 != null){
            ColorImage grenadeImage = scene4.render(grenadeXResolution, grenadeYResolution, 25, 1);
            String filename2 = "scene4.png";
            saveImage(filename2, grenadeImage);
        }
        
        //Save the image out as a png
        System.out.println("Saving file...");
        String filename = "scene2.png";
        saveImage(filename, shapesImage);
        
        for(int frame = 0; frame < frames; frame++){
            //Create the scene. You can change this when you make other scene creation methods to select
            //which scene to render.
            System.out.println("Creating scene...");
            //Scene s = SceneCreator.scene2(xResolution, yResolution);
            double time = (double) frame / frames;
            Scene scene3 = null;
            try{
                scene3 = SceneCreator.scene3(carXResolution, carYResolution, time);
            }catch(IOException e){
                System.out.println("Problem loading OBJ: " + e.getMessage());
            }
            
            //Render the scene
            System.out.println("Rendering scene...");
            if(scene3 != null){
                ColorImage carFrame = scene3.render(carXResolution, carYResolution, 16, time);
                BufferedImage buffImg = new BufferedImage(carXResolution, carYResolution, BufferedImage.TYPE_INT_RGB);
                //for every pixel, read the color of my colorimage and write it 
                //into a bufferedimage so jcodec can use that instead
                for(int x = 0; x < carXResolution; x++){
                    for (int y = 0; y < carYResolution; y++) {
                        buffImg.setRGB(x, carYResolution - 1 - y, carFrame.getColor(x, y).toARGB());
                        //we define y = 0 as the bottom in our other class, but bufferedImages 
                        //have y=0 as the top, so yResolution - 1 - y is to flip it 
                        //around for the bufferedImage
                    }
                }
                carFrames.add(buffImg);
            }
            
            /* Simple image write. Use this to test if image writing is broken.
               If this doesn't work, that means something is wrong with your Java installation.
               If it DOES work, and you get a color gradient image written out as "testGradient.png",
               but the normal saveImage does not work, that means something is wrong with your raytracing code.
               */
            //saveTestImage();
        }
        
        saveVideo("carSpin.mp4", carFrames);
        System.out.println("Done");
    }
    
    /**
     * Reads in each pixel from a ColorImage, and then writes the image out to a PNG file.
     */
    public static void saveImage(String filename, ColorImage image){
        try {
            BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            for(int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    //This line reverses the y axis. Use the following line instead if your image is upside down.
                    bi.setRGB(x,image.getHeight()-1-y,image.getColor(x,y).toARGB());
                    //bi.setRGB(x,y,image.getColor(x,y).toARGB());
                }
            }
            ImageIO.write(bi, "PNG", new File(filename));
            
        } catch(Exception e) {
            System.out.println("Problem saving image: " + filename);
            System.out.println(e);
            System.exit(1);
        }
    }
    
    public static void saveVideo(String filename, List<BufferedImage> frames) {
        try{
            //a sequence encoder is a utility used to convert a sequence of image files or 
            //frames into an encoded video file
            AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(new File(filename), 24); //creates the sequence encoder
            //opens an MP4 file for writing, first parameter is final mp4 filename, second parameter is fps
            for(int i = 0; i < frames.size(); i++){ //loops through list of frames
                encoder.encodeImage(frames.get(i)); // encodes every bufferedimage frame one by one
            }
            encoder.finish(); //closes the file and finishes mp4, otherwise itd be corrupted
        }catch(IOException e){
            System.out.println("Problem saving video: " + e.getMessage());
        }
    }
    
    /**
     * Simpler version of the saveImage method for testing. Doesn't require integration with ColorImage, just writes
     * a gradient of colors out to an image to make sure the BufferedImage, ImageIO, and File libraries are working
     * as expected.
     */
    public static void saveTestImage(){
        try{
               BufferedImage biTest = new BufferedImage(250,200,BufferedImage.TYPE_INT_RGB);
               for(int x = 0; x < 250; x++){
                   for(int y = 0; y < 200; y++){
                       biTest.setRGB(x,200-1-y,(x << 16) | (y << 8) | (0 << 0));
                    }
                }
                ImageIO.write(biTest, "PNG", new File("testGradient.png"));
                
        }catch(Exception e){
            System.out.println("Problem saving test gradient image");
            System.out.println(e);
            System.exit(1);
        }
    }
}