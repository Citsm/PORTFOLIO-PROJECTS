public class ColorImage{
    //column-major order
    private Color[][] colorArray;
    private int width, height;
    
    public ColorImage(int newWidth, int newHeight){
        width = newWidth;
        height = newHeight;
        
        colorArray = new Color[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                colorArray[x][y] = new Color(0, 0, 0);
            }
        }
    }
    
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    
    public Color getColor(int col, int row){
        return colorArray[col][row];
    }
    
    public void setColor(int col, int row, Color c){
        colorArray[col][row] = c;
    }
}