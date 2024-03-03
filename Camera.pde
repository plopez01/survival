class Camera extends WorldObject {
  float zoom, resolution;
  float moveSensitivity, zoomSensitivity;

  RangeConstrain zoomConstrain;

  Camera(float resolution, float zoom, RangeConstrain zoomConstrain, float moveSensitivity, float zoomSensitivity) {
    this.resolution = resolution;
    this.zoom = zoom;
    
    this.zoomConstrain = zoomConstrain;

    this.moveSensitivity = moveSensitivity;
    this.zoomSensitivity = zoomSensitivity;
  }

  void setZoom(float zoom) {
    this.zoom = zoomConstrain.enforce(zoom);
  }
  
  float getZoomPosition(){
    return zoomConstrain.getPosition(zoom);
  }

  void updateZoom(Mouse mouse, float change) {
    float newZoom = zoom + change*zoomSensitivity;

    if (zoomConstrain.inBounds(newZoom)) {
      PVector mouseTarget = getWorldMouse(mouse);
      
      zoom = newZoom;
      
      PVector newMouseTarget = getWorldMouse(mouse);
      
      transform.add(mouseTarget.sub(newMouseTarget));
    }
  }
  
  PVector getWorldMouse(Mouse mouse){
    return multiplyVectors(mouse.getMouseDistFromCenter(), getWorldViewportSize());
  }
  
  PVector getWorldViewportSize(){
    return toWorldSpace(new PVector(width, height));
  }
  
  PVector toScreenSpace(PVector in) {
    return new PVector(in.x * -zoom * resolution, in.y * -zoom * resolution, in.z * zoom * resolution);
  }
  
  PVector toWorldSpace(PVector in) {
    return new PVector(in.x / (-zoom * resolution), in.y / (-zoom * resolution), in.z / (zoom * resolution));
  }
  
  PVector toRelativeScreenSpace(PVector in){
    PVector screenSpace = toScreenSpace(PVector.add(transform, in));
    return new PVector(screenSpace.x +((float)width/2), screenSpace.y +((float)height/2), screenSpace.z);
  }

  void translate(PVector vec) {
    transform.add(vec);
  }
}
