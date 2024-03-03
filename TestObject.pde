class TestObject extends WorldObject implements Renderable {
  void render() {
    noStroke();
    square(transform.x, transform.y, transform.z);
  }
}
