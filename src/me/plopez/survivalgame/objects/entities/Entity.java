package me.plopez.survivalgame.objects.entities;

import me.plopez.survivalgame.objects.WorldObject;
import processing.core.PVector;

public abstract class Entity extends WorldObject {
    abstract public void commandMove(PVector position);
}
