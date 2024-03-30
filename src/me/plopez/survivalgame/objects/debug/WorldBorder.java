package me.plopez.survivalgame.objects.debug;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.Renderable;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public class WorldBorder extends WorldObject implements Renderable {
    public void render() {
        sketch.rectMode(CENTER);
        sketch.fill(255, 0, 0);
        sketch.square(0, 0, 1);
        sketch.rectMode(CORNER);
    }

    public void renderText(){

    }
}
