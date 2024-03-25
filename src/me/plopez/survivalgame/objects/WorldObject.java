package me.plopez.survivalgame.objects;


import java.io.Serializable;
import java.util.UUID;
import me.plopez.survivalgame.vector.VectorF;

public abstract class WorldObject implements Serializable {
    public VectorF transform = new VectorF();
    private final UUID id = UUID.randomUUID();

    public WorldObject() {
        // Default scale
        transform.z = 1;
    }

    public void translate(VectorF vec) {
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
