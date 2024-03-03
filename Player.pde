class Player extends WorldObject implements Renderable, Commandable {
  String name;
  float speed;
  color c;
  
  Player(String name, float speed, color c) {
    this.name = name;
    this.speed = speed;
    this.c = c;
    
    transform.z = 0.03;
  }
  
  void commandMove(PVector to) {
    
  }
  
  void render() {
    fill(c);
    square(transform.x, transform.y, transform.z);
    //textSize(1);
    //text(name, transform.x, transform.y + 1);
  }
}
