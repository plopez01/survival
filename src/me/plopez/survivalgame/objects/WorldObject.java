package me.plopez.survivalgame.objects;

import processing.core.PVector;

public abstract class WorldObject {
  public PVector transform = new PVector();

  public WorldObject() {
    // Default scale
    transform.z = 1;
  }

  public void translate(PVector vec) {
    transform.x += vec.x;
    transform.y += vec.y;
  }

  public void scale(float value) {
    transform.z += value;
  }
}
