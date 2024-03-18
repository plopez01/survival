package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PVector;

public abstract class UIElement implements Renderable {
    PVector position;
    PVector size;

    public final void render() {}
    public final void renderText() {}
}
