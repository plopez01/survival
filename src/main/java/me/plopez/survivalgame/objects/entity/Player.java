package me.plopez.survivalgame.objects.entity;

import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.rendering.Terrain;
import me.plopez.survivalgame.rendering.World;
import processing.core.PApplet;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.*;

public class Player extends Entity implements Renderable {
    String name;
    int c;

    public Player(String name, float speed, int c) {
        super(speed);

        this.name = name;
        this.c = c;
    }

    public void render() {
        sketch.rectMode(CENTER);
        sketch.fill(c);
        sketch.square(0, 0, 1);
        sketch.rectMode(CORNER);
    }

    public void renderText() {
        sketch.textSize(16);
        sketch.textAlign(CENTER);
        sketch.fill(c);
        sketch.text(name, 0, 0 + sketch.textAscent() * 2);
        sketch.textAlign(CORNER);
    }

    public String getName() {
        return name;
    }

}
