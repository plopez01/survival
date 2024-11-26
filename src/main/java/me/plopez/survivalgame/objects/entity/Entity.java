package me.plopez.survivalgame.objects.entity;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.World;
import processing.core.PApplet;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;

public abstract class Entity extends WorldObject {
    float speed;

    Entity(float speed) {
        this.speed = speed;
    }

    PVector target = transform;
    long startCommandTime;
    float travelledDistance = 0;

    PVector startPos = transform;

    public void setTarget(PVector to) {
        target = new PVector(to.x, to.y);
        startPos = new PVector(transform.x, transform.y);
        startCommandTime = sketch.millis();
        travelledDistance = 0;
    }

    @Override
    public void tick(World world) {
        float comandProgress = PApplet.constrain(travelledDistance / (PVector.dist(startPos, target)+1), 0, 1);

        System.out.println(comandProgress);

        PVector newPos = PVector.lerp(startPos, target, comandProgress);
        travelledDistance += speed / (sketch.frameRate);

        transform.x = newPos.x;
        transform.y = newPos.y;
    }
}
