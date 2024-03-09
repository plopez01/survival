package me.plopez.survivalgame.input;

import processing.core.PApplet;
import processing.core.PVector;

public class Mouse {
    public static PVector getMouseDistFromCenter(PApplet sketch) {
        return new PVector(
                (float) (sketch.width / 2 - sketch.mouseX) / sketch.width,
                (float) (sketch.height / 2 - sketch.mouseY) / sketch.height);
    }
}
