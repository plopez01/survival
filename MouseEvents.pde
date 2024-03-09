PVector holdOrigin = new PVector();

void mouseWheel(MouseEvent event) {
  client.camera.updateZoom(mouse, -event.getCount());
}

void mousePressed(){
  holdOrigin = new PVector(mouseX, mouseY);
}

void mouseReleased(){
}

void mouseClicked(){
  //player.commandMove(camera.getRelativeWorldMouse(mouse));
}

void mouseDragged(){
  PVector mousePos = new PVector(mouseX, mouseY);
  
  PVector displacement = mousePos.sub(holdOrigin);
  client.camera.translate(client.camera.toWorldSpace(displacement));
  holdOrigin = new PVector(mouseX, mouseY);
}
