class Terrain {
  color[] tileSet;
  float noiseScale;
  
  Terrain(color[] tileSet, float noiseScale){
    this.tileSet = tileSet;
    this.noiseScale = noiseScale;
  }
  
  void generate(){
    
  }
  
  void drawAt(PVector pos, float scale) {
    for (int xoff = 0; xoff < width/scale; xoff++) {
      for (int yoff = 0; yoff < height/scale; yoff++) {
        float x = pos.x + xoff;
        float y = pos.y + yoff;
        
        float terrainHeight = noise(x/noiseScale, y/noiseScale);
        if (colorRegion(terrainHeight, color(0, 0, 255), color(70, 70, 255), 0, 0.3));
        else if (colorRegion(terrainHeight, color(70, 70, 255), color(200, 200, 100), 0.3, 0.35));
        else if (colorRegion(terrainHeight, color(200, 200, 100), color(40, 120, 40), 0.35, 0.6));
        else if (colorRegion(terrainHeight, color(40, 120, 40), color(80, 60, 60), 0.6, 0.65));
        else if (colorRegion(terrainHeight, color(80, 60, 60), color(180), 0.65, 0.8));
        else fill(255*terrainHeight);
        
        rect(xoff*scale, yoff*scale, scale, scale);
      }
    }
  }
}

boolean colorRegion(float terrainHeight, color cfrom, color cto, float from, float to){
  if (terrainHeight >= from && terrainHeight < to) {
    fill(lerpColor(cfrom, cto, (terrainHeight-from)/(to-from)));
    return true;
  } else return false;
}