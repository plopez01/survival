class Mouse {
  
  PVector getMouseDistFromCenter() {
    return new PVector((float)(width/2 - mouseX)/width, (float)(height/2 - mouseY)/height);
  }
}
