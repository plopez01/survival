package me.plopez.survivalgame.log;

import me.plopez.survivalgame.objects.debug.WorldBorder;
import me.plopez.survivalgame.rendering.CameraRenderer;
import me.plopez.survivalgame.rendering.World;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.*;

public class Debug {
    String debugText;

    public void add(String key, Object value) {
        debugText += "\n" + key + ": " + value;
    }

    public void add(String key, PVector vec) {
        debugText += "\n" + key + ": (" + vec.x + ", " + vec.y + ", " + vec.z + ")";
    }

    public void showPointer() {
        sketch.fill(255, 0, 0, 100);
        sketch.rectMode(CENTER);
        sketch.square((float) sketch.width / 2, (float) sketch.height / 2, 10);
        sketch.rectMode(CORNER);
    }
    public void render() {
        sketch.fill(255);
        sketch.textSize(16);
        sketch.text(debugText, 0, 0);

        debugText = "";
    }
}
