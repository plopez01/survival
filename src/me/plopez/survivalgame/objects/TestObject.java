package me.plopez.survivalgame.objects;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PApplet;

import static processing.core.PConstants.*;
import static me.plopez.survivalgame.Globals.sketch;

public class TestObject extends WorldObject implements Renderable {
    public void render() {
        sketch.rectMode(CENTER);
        sketch.fill(255, 0, 0);
        sketch.square(0, 0, 1);
        sketch.rectMode(CORNER);
    }

    public void renderText() {
        sketch.textSize(16);
        sketch.textAlign(CENTER);
        sketch.fill(255, 0, 0);
        sketch.text("test", 0, 0 + sketch.textAscent() * 2);
        sketch.textAlign(CORNER);
    }

    public void renderUnscaled() {

    }

}
