package me.plopez.survivalgame.objects;

import processing.core.PVector;

import java.io.Serializable;
import java.util.UUID;

public abstract class WorldObject implements Serializable {
    public PVector transform = new PVector();
    private final UUID id = UUID.randomUUID();

    public WorldObject() {
        // Default scale
        transform.z = 1;
    }

    public void translate(PVector vec) {
        transform.x += vec.x;
        transform.y += vec.y;
    }

    public void scale(float value) {
        transform.z += value;
    }

    public UUID getId() {
        return id;
    }
}
