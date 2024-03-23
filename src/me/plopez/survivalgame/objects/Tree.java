package me.plopez.survivalgame.objects;

import me.plopez.survivalgame.rendering.Renderable;

import static me.plopez.survivalgame.Globals.sketch;

public class Tree extends WorldObject implements Renderable {
    @Override
    public void render() {
        sketch.fill(0, 80, 0);
        sketch.triangle(-1, 1, 0, -1, 1, 1);
    }

    @Override
    public void renderText() {

    }
}
