package me.plopez.survivalgame.objects.entities;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.vector.VectorF;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.*;

public class Player extends Entity implements Renderable {
    String name;
    float speed;
    int c;

    VectorF target;

    long startCommandTime;
    float targetDistance = 1;

    VectorF startPos = transform;

    public Player(String name, float speed, int c) {
        this.name = name;
        this.speed = speed;
        this.c = c;

        target = transform;
    }

    public void commandMove(VectorF to) {
        target = new VectorF(to.x, to.y);
        startPos = new VectorF(transform.x, transform.y);
        startCommandTime = sketch.millis();
        targetDistance = VectorF.dist(startPos, target) * speed;
    }

    public void render() {
        sketch.rectMode(CENTER);
        sketch.fill(c);
        sketch.square(0, 0, 1);
        sketch.rectMode(CORNER);

        long spentTime = sketch.millis() - startCommandTime;

        float comandProgress = (float) spentTime / targetDistance;
        if (comandProgress < 1) {
            VectorF newPos = VectorF.lerp(startPos, target, comandProgress);
            transform.x = newPos.x;
            transform.y = newPos.y;
        }
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
