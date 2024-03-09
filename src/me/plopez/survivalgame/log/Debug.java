package me.plopez.survivalgame.log;

import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.*;

public class Debug {
  PApplet sketch;
  String debugText;
  
  public Debug(PApplet sketch) {
    this.sketch = sketch;
  }
  
  public void add(String key, Object value) {
    debugText += "\n" + key + ": " + value;
  }

  public void add(String key, PVector vec) {
    debugText += "\n" + key + ": (" + vec.x + ", " + vec.y + ", " + vec.z + ")";
  }

  public void showPointer(){
    sketch.fill(255, 0, 0, 100);
    sketch.rectMode(CENTER);
    sketch.square((float) sketch.width / 2, (float) sketch.height / 2, 10);
    sketch.rectMode(CORNER);
  }

  public void render(){
    sketch.fill(255);
    sketch.textSize(16);
    sketch.text(debugText, 0, 0);
    
    debugText = "";
  }
}
