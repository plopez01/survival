class Terrain {
  int terrainSize, translationOffset;
  float heightOffset;
  
  int baseDetail, zoomDetail;
  
  Terrain(int terrainSize, int translationOffset, float heightOffset, int baseDetail, int zoomDetail){
    this.terrainSize = terrainSize;
    this.translationOffset = translationOffset;
    this.heightOffset = heightOffset;
    
    this.baseDetail = baseDetail;
    this.zoomDetail = zoomDetail;
  }
  
  void renderAt(Camera cam) {
    stroke(255, 20);
    for (int xoff = 0; xoff < width/cam.resolution; xoff++) {
      for (int yoff = 0; yoff < height/cam.resolution; yoff++) {
        float x = (cam.transform.x*cam.zoom + xoff) -((float)width/cam.resolution/2);
        float y = (cam.transform.y*cam.zoom + yoff) -((float)height/cam.resolution/2);
        
        noiseDetail(round(zoomDetail*cam.getZoomPosition()) + baseDetail);
        
        float distanceFromOrigin = (abs(x/cam.zoom)+abs(y/cam.zoom))/terrainSize+heightOffset;
        float terrainHeight = noise(x/cam.zoom+translationOffset, y/cam.zoom+translationOffset)/distanceFromOrigin;
        
        if (colorRegion(terrainHeight, color(0, 0, 255), color(70, 70, 255), 0, 0.3));
        else if (colorRegion(terrainHeight, color(70, 70, 255), color(200, 200, 100), 0.3, 0.35));
        else if (colorRegion(terrainHeight, color(200, 200, 100), color(40, 120, 40), 0.35, 0.6));
        else if (colorRegion(terrainHeight, color(40, 120, 40), color(80, 60, 60), 0.6, 0.65));
        else if (colorRegion(terrainHeight, color(80, 60, 60), color(180), 0.65, 0.8));
        else fill(255*terrainHeight);
        
        rect(xoff*cam.resolution, yoff*cam.resolution, cam.resolution, cam.resolution);
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
