class Debug {
  String debugText;
  
  Debug() { }
  
  void add(String key, Object value) {
    debugText += "\n" + key + ": " + value;
  }
  
  void add(String key, PVector vec) {
    debugText += "\n" + key + ": (" + vec.x + ", " + vec.y + ", " + vec.z + ")";
  }
  
  void showPointer(){
    fill(255, 0, 0, 100);
    rectMode(CENTER);
    square(width/2, height/2, 10);
    rectMode(CORNER);
  }
  
  void render(){
    fill(255);
    textSize(16);
    text(debugText, 0, 0);
    
    debugText = "";
  }
}
