package me.plopez.survivalgame.objects.cell;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.World;
import processing.core.PVector;

public class Cell extends WorldObject {
    public void translate(PVector vec) {
        transform.x += (int) vec.x;
        transform.y += (int) vec.y;
    }

    public void scale(float value) {
        transform.z = value;
        //TODO: move this
        throw new IllegalStateException("Cells cannot be resized");
    }
}
