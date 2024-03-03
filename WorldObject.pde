abstract class WorldObject {
  PVector transform = new PVector();
  
  WorldObject() {
    // Default scale
    transform.z = 1;
  }
  
  void translate(PVector vec) {
    transform.x += vec.x;
    transform.y += vec.y;
  }
  
  void scale(float value) {
    transform.z += value;
  }
}
