boolean isHoldingMouse = false;
PVector holdOrigin = new PVector();

void mouseWheel(MouseEvent event) {
  //camera.updateZoom(event.getCount());
  camera.position.add(width/2, height/2);
}

void mousePressed(){
  holdOrigin = new PVector(mouseX, mouseY);
}

void mouseReleased(){
  isHoldingMouse = false;
}

void mouseDragged(){
  PVector mousePos = new PVector(mouseX, mouseY);
  
  if (true) {
    PVector displacement = mousePos.sub(holdOrigin);
    camera.position.add(displacement.mult(-0.1));
    holdOrigin = new PVector(mouseX, mouseY);
  } else {
    holdOrigin = new PVector(mouseX, mouseY);
    isHoldingMouse = true;
  }
}