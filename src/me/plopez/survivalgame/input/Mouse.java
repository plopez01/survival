package me.plopez.survivalgame.input;

import processing.core.PApplet;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;

public class Mouse {
    public static PVector mousePos(){
        return new PVector(sketch.mouseX, sketch.mouseY);
    }

    public static PVector relativePos(){
        return new PVector((float) sketch.mouseX / sketch.width, (float) sketch.mouseY / sketch.height);
    }

    public static PVector getMouseDistFromCenter(PApplet sketch) {
        return new PVector(
                (float) (sketch.mouseX - sketch.width / 2 ) / sketch.width,
                (float) (sketch.mouseY - sketch.height / 2) / sketch.height);
    }
}
