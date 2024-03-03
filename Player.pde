class Player extends WorldObject implements Renderable, Commandable {
  String name;
  float speed;
  color c;

  PVector target;

  long startCommandTime;
  float targetDistance = 1;

  PVector startPos = transform;
  Player(String name, float speed, color c) {
    this.name = name;
    this.speed = speed;
    this.c = c;

    target = transform;
  }

  void commandMove(PVector to) {
    target = new PVector(-to.x, -to.y);
    startPos = new PVector(transform.x, transform.y);
    startCommandTime = millis();
    targetDistance = PVector.dist(startPos, target)*speed;
  }

  void render() {
    rectMode(CENTER);
    fill(c);
    square(0, 0, 1);
    rectMode(CORNER);

    long spentTime = millis()-startCommandTime;

    float comandProgress = (float)spentTime/targetDistance;
    if (comandProgress < 1) {
      PVector newPos = PVector.lerp(startPos, target, comandProgress);
      transform.x = newPos.x;
      transform.y = newPos.y;
    }
  }

  void renderText() {
    textSize(16);
    textAlign(CENTER);
    fill(c);
    text(name, 0, 0 + textAscent()*2);
    textAlign(CORNER);
  }

  void renderUnscaled() {
  }
}
