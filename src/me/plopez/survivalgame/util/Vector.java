package me.plopez.survivalgame.util;

import processing.core.PVector;

public class Vector {
  public static PVector multiplyVectors(PVector a, PVector b) {
    return new PVector(a.x * b.x, a.y * b.y, a.z * b.z);
  }
}

