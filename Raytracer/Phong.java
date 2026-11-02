public class Phong extends Material {
    private Color diffuse;
    private Color specular;
    private double exponent;

    public Phong(Color diff, Color spec, double exp){
        diffuse = diff;
        specular = spec;
        exponent = exp;
    }

    public Color computeLighting(Intersection i, Ray viewingRay, Light li){
        Vector lightDir = li.computeLightDirection(i.getPosition());
        //how directly the surface faces the light (1 = straight on, 0 = sideways) 
        double dot = i.getNormal().dot(lightDir);
    
        //negative means the surface is facing away from the light...so no light
        if(dot < 0){
            return new Color(0, 0, 0);
        }
    
        //diffuse part: the surface color, dimmed by the angle to the light and tinted by the lights color
        Color dimmed = diffuse.shade(new Color(dot, dot, dot));
        Color diffuseColor = dimmed.shade(li.computeLightColor(i.getPosition()));
    
        //specular part: the shiny highlight, same reflection formula as in computeVisibleColor
        Vector normal = i.getNormal();
        Vector mirrorDir = normal.scale(2 * normal.dot(lightDir)).subtract(lightDir).normalize();
        
        //how close the camera is to looking down the bounce direction
        double cosSpecAngle = viewingRay.getDirection().scale(-1).dot(mirrorDir);
        
        //negative means the camera isnt near the bounce...so no highlight
        if(cosSpecAngle < 0){
            return diffuseColor;
        }
    
        //raising it to the exponent makes the highlight more focused, bigger exponent 
        //means smaller, sharper highlight
        double specCoeff = Math.pow(cosSpecAngle, exponent);
    
        Color highlightColor = li.computeLightColor(i.getPosition())
                                 .shade(new Color(specCoeff, specCoeff, specCoeff))
                                 .shade(specular);
    
        //final color is the diffuse color with the highlight blended on top
        return diffuseColor.tint(highlightColor);
    }
}