package me.plopez.survivalgame.objects.entities;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.vector.VectorF;

public abstract class Entity extends WorldObject {
    abstract public void commandMove(VectorF position);
}
