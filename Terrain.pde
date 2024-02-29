class Terrain {
  color[] tileSet;
  int scale;
  
  Terrain(color[] tileSet, int scale){
    this.tileSet = tileSet;
    this.scale = scale;
  }
  
  void generate(){
    
  }
  
  void renderAt(PVector pos) {
    for (int xoff = 0; xoff < width/scale; xoff++) {
      for (int yoff = 0; yoff < height/scale; yoff++) {
        float x = pos.x + xoff;
        float y = pos.y + yoff;
        fill(255*noise(x, y));
        rect(x, y, scale, scale);
      }
    }
  }
}