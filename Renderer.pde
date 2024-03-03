class Renderer implements Renderable {
  Camera cam;
  ArrayList<Renderable> renderables = new ArrayList<>();
  
  Renderer(Camera cam) {
    this.cam = cam;
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
        strokeWeight(g.strokeWeight/screenSpace.z);
        textSize(constrain((float)g.textSize/screenSpace.z, 1, 1000000));
        translate(screenSpace.x, screenSpace.y);
        scale(screenSpace.z);
        
        renderable.render();
        
        popMatrix();
      } else {
        renderable.render();
      }
    }
  }
}
