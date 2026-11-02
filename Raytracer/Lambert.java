public class Lambert extends Material{
    Color diffuseColor;
    
    public Lambert(Color c){
        diffuseColor = c;
    }
    
    public Color computeLighting(Intersection i, Ray viewingRay, Light li){
        Vector direction = li.computeLightDirection(i.getPosition()).normalize();
        
        double dotProduct = i.getNormal().dot(direction);
        if(dotProduct < 0){
            return new Color(0, 0, 0);
        }
        
        Color dim = diffuseColor.scale(dotProduct);
        return dim.shade(li.computeLightColor(i.getPosition()));
    }
}