import java.io.*;
import java.util.*;

//https://www.youtube.com/watch?v=YwBHlo_T8Ao
//https://youtu.be/FUI7HEEz9B0?si=lLMggY1Klfxm---h
public class ObjParser{
    //static method so you can call it on the class, dont need an object
    //throw is incase it fails to get the file
    public static List<Surface> parse(String filepath, Material mat) throws IOException{
        //.obj file format is
        //v x, y, z, [w] where xyz are decimals and w (weight) is optional (defaults to 1.0). these are a list geometric vertices
        //im not gonna be using weight
        //f 1/4/8 4/8/10 30/45/20 where the first number is the vertex index, the second is the texture 
        //coordinate index (not using), and the third number is the normal index (not using)
        List<Point> vertices = new ArrayList<>(); //list full of vertices of obj
        List<int[]> faces = new ArrayList<>(); //list would be full of triangulated face indices [v0, v1, v2]
        
        //https://www.geeksforgeeks.org/java/difference-between-bufferedreader-and-filereader-in-java/
        //https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
        //a buffer is a small portion of device memory that holds some data, they usually use
        //the RAM to store that data which makes it much faster to access
        //a buffered reader can take in any kind of reader to read data and creates an input buffer
        //this one takes in a filereader which reads text from a file directly, the bufferedreader improves
        //performance bc it doesnt access the hard drive directly every time to read data
        try(BufferedReader obj = new BufferedReader(new FileReader(filepath))){
            String line;
            while((line = obj.readLine()) != null){
                line = line.trim();
                if(line.startsWith("v ")){
                    //the split() method breaks a string into an array of substrings based on a given regex ("a character or sequence of characters used to define boundaries")
                    //https://stackoverflow.com/questions/225337/how-to-split-a-string-with-any-whitespace-chars-as-delimiters
                    //https://stackoverflow.com/questions/15625629/regex-expressions-in-java-s-vs-s
                    String[] splitLine = line.split("\\s+");
                    vertices.add(new Point(
                        Double.parseDouble(splitLine[1]),
                        Double.parseDouble(splitLine[2]),
                        Double.parseDouble(splitLine[3])
                    ));
                    //so in a line in the obj like "v 1.00384903 3.04390 7.932220"
                    //splitLine[0] is v
                    //splitLine[1] will be 1.00384903
                    //splitLine[2] will be 3.04390
                    //splitLine[3] will be 7.932220
                    //and those split strings get turned into doubles and added to my vertices list
                }else if(line.startsWith("f ")){
                    parseFace(line, faces);
                }
            }
        }
        
        List<Surface> triangles = new ArrayList<>();
        for(int i = 0; i < faces.size(); i++){
            int[] face = faces.get(i);
            Point v0 = vertices.get(face[0] - 1); //needs -1 bc obj files are 1-indexed but java is 0-indexes
            Point v1 = vertices.get(face[1] - 1);
            Point v2 = vertices.get(face[2] - 1);
            triangles.add(new Triangle(v0, v1, v2, mat));
        }
        return triangles;
    }
    
    //private bc its only used in the general parse method, this just handles the faces
    //in the obj file in paricular
    private static void parseFace(String line, List<int[]> faces){
        //example format for faces line in an objs texted it
        //f 123/18326/16661 23160/18329/16664 23161/18330/16665 124/18327/16662
        //in this case, this example line from my obj is a quad bc there are four given vertices
        //but im gonna split the quads into triangles since i do not have a quad class
        String[] splitLine = line.split("\\s+"); //same spliiting at whitespaces in parse()
        List<Integer> faceVerts = new ArrayList<>(); //list holding the vertices to connect
        for(int i = 1; i < splitLine.length; i++){
            String[] vertexIndices = splitLine[i].split("/"); //split again incase the lines give more data
            //on texture coordinates and stuff, i only need the vertices
            faceVerts.add(Integer.parseInt(vertexIndices[0]));
        }
        
        //loop to handle quads (breaks it into two triangles)
        for(int i = 1; i < faceVerts.size() - 1; i++){
            //given a line f 1/3/5 2/67/3 3/3/53 4/29/3
            //faceVerts is [1, 2, 3, 4]
            //adds [1, 2, 3] and [1, 3, 4] to faces
            //in an obj file the vertices given in f lines assuming the obj is modeled well will be in a clockwise
            //or counterclockwise order. assuming that faceVerts.get(0) is the splitting corner both triangles
            //will share after the squad is cut, these are the vertices that get grouped together:
            faces.add(new int[]{faceVerts.get(0), faceVerts.get(i), faceVerts.get(i + 1)});
            //if this doesnt work, the quads had an issue on exporting the obj and the vertices are in a strange order
        }
    }
}