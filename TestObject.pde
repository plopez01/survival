class TestObject extends WorldObject implements Renderable {

  void render() {  
    rectMode(CENTER);
    fill(255, 0, 0);
    square(0, 0, 1);
    rectMode(CORNER);
  }
  
  void renderText() {
    textSize(16);
    textAlign(CENTER);
    fill(255, 0, 0);
    text("test", 0, 0 + textAscent()*2);
    textAlign(CORNER);
  }
  
  void renderUnscaled() {
    
  }

}
