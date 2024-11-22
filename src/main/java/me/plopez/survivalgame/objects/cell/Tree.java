package me.plopez.survivalgame.objects.cell;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.Renderable;

import static me.plopez.survivalgame.Globals.sketch;

public class Tree extends Cell implements Renderable {
    @Override
    public void render() {
        sketch.fill(0, 80, 0);
        sketch.triangle(-1, 1, 0, -1, 1, 1);
    }

    @Override
    public void renderText() {

    }
}
