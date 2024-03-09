package me.plopez.survivalgame.entities;

import me.plopez.survivalgame.entities.Commandable;
import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.*;

public class Player extends WorldObject implements Renderable, Commandable {
  PApplet sketch;
  String name;
  float speed;
  int c;

  PVector target;

  long startCommandTime;
  float targetDistance = 1;

  PVector startPos = transform;
  public Player(PApplet sketch, String name, float speed, int c) {
    this.sketch = sketch;

    this.name = name;
    this.speed = speed;
    this.c = c;

    target = transform;
  }

  public void commandMove(PVector to) {
    target = new PVector(-to.x, -to.y);
    startPos = new PVector(transform.x, transform.y);
    startCommandTime = sketch.millis();
    targetDistance = PVector.dist(startPos, target)*speed;
  }

  public void render() {
    sketch.rectMode(CENTER);
    sketch.fill(c);
    sketch.square(0, 0, 1);
    sketch.rectMode(CORNER);

    long spentTime = sketch.millis()-startCommandTime;

    float comandProgress = (float)spentTime/targetDistance;
    if (comandProgress < 1) {
      PVector newPos = PVector.lerp(startPos, target, comandProgress);
      transform.x = newPos.x;
      transform.y = newPos.y;
    }
  }

  public void renderText() {
    sketch.textSize(16);
    sketch.textAlign(CENTER);
    sketch.fill(c);
    sketch.text(name, 0, 0 + sketch.textAscent()*2);
    sketch.textAlign(CORNER);
  }

  public void renderUnscaled() {
  }
}
