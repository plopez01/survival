package me.plopez.survivalgame.input;

import me.plopez.survivalgame.vector.VectorF;
import processing.core.PApplet;

import static me.plopez.survivalgame.Globals.sketch;

public class Mouse {
    public static VectorF mousePos(){
        return new VectorF(sketch.mouseX, sketch.mouseY);
    }

    public static VectorF relativePos(){
        return new VectorF((float) sketch.mouseX / sketch.width, (float) sketch.mouseY / sketch.height);
    }

    public static VectorF getMouseDistFromCenter(PApplet sketch) {
        return new VectorF(
                (float) (sketch.mouseX - sketch.width / 2 ) / sketch.width,
                (float) (sketch.mouseY - sketch.height / 2) / sketch.height);
    }
}
