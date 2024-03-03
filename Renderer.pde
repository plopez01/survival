class Renderer implements Renderable {
  Camera cam;
  ArrayList<Renderable> renderables = new ArrayList<>();
  float globalScale;
  
  Renderer(Camera cam, float globalScale) {
    this.cam = cam;
    this.globalScale = globalScale;
  }
  
  void add(Renderable renderable){
    renderables.add(renderable);
  }
  
  void render(){
    for (Renderable renderable : renderables) {
      if (renderable instanceof WorldObject) {
        WorldObject worldObject = (WorldObject) renderable;
        
        pushMatrix();
        
        PVector screenSpace = cam.toRelativeScreenSpace(worldObject.transform);
        print("Relative:");
        println(screenSpace);
        strokeWeight(g.strokeWeight/screenSpace.z);
        translate(screenSpace.x, screenSpace.y);
        
        renderable.renderUnscaled();
        
        pushMatrix();
        scale(screenSpace.z*globalScale/g.textSize);
        renderable.renderText();
        popMatrix();
        
        scale(screenSpace.z*globalScale);
        
        renderable.render();
        
        popMatrix();
      } else {
        renderable.render();
        renderable.renderText();
        renderable.renderUnscaled();
      }

    }
  }
  
  void renderText(){}  
  void renderUnscaled(){}
}
