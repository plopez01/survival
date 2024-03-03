PVector holdOrigin = new PVector();

void mouseWheel(MouseEvent event) {
  camera.updateZoom(mouse, -event.getCount());
  println(event.getCount());
}

void mousePressed(){
  holdOrigin = new PVector(mouseX, mouseY);
}

void mouseReleased(){
}

void mouseDragged(){
  PVector mousePos = new PVector(mouseX, mouseY);
  
  PVector displacement = mousePos.sub(holdOrigin);
  camera.translate(camera.toWorldSpace(displacement));
  holdOrigin = new PVector(mouseX, mouseY);
}
