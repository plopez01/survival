class Debug {
  Debug() { }
  
  void draw(){
    fill(255);
    textSize(18);
    text(frameRate, 0, 16);
  }
}
