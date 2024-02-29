class Camera {
  PVector position = new PVector();
  
  float zoom;
  float moveSensitivity, zoomSensitivity;
  
  Camera(float zoom, float moveSensitivity, float zoomSensitivity) {
    this.zoom = zoom;
    
    this.moveSensitivity = moveSensitivity;
    this.zoomSensitivity = zoomSensitivity;
  }
  
  void setZoom(float zoom) {
    this.zoom = zoom;
  }
  
  void updateZoom(float change) {
    this.zoom += change*zoomSensitivity;
  }
}
