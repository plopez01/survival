package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Renderer implements Renderable {
  PApplet sketch;
  Camera cam;
  ArrayList<Renderable> renderables = new ArrayList<>();
  float globalScale;
  
  public Renderer(PApplet sketch, Camera cam, float globalScale) {
    this.sketch = sketch;
    this.cam = cam;
    this.globalScale = globalScale;
  }
  
  void add(Renderable renderable){
    renderables.add(renderable);
  }
  
  public void render(){
    for (Renderable renderable : renderables) {
      if (renderable instanceof WorldObject) {
        WorldObject worldObject = (WorldObject) renderable;

        sketch.pushMatrix();

        PVector screenSpace = cam.toRelativeScreenSpace(worldObject.transform);

        sketch.strokeWeight(sketch.g.strokeWeight/screenSpace.z);
        sketch.translate(screenSpace.x, screenSpace.y);
        
        renderable.renderUnscaled();

        sketch.pushMatrix();
        sketch.scale(screenSpace.z*globalScale/sketch.g.textSize);
        renderable.renderText();
        sketch.popMatrix();

        sketch.scale(screenSpace.z*globalScale);
        
        renderable.render();

        sketch.popMatrix();
      } else {
        renderable.render();
        renderable.renderText();
        renderable.renderUnscaled();
      }

    }
  }
  
  public void renderText(){}
  public void renderUnscaled(){}
}
