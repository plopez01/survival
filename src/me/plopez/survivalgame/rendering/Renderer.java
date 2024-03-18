package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.objects.WorldObject;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static me.plopez.survivalgame.Globals.sketch;

public class Renderer implements Renderable {
    ArrayList<Renderable> renderables = new ArrayList<>();
    float globalScale;

    public Renderer(float globalScale) {
        this.globalScale = globalScale;
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }
    public void add(List<Renderable> newList) {
        renderables.addAll(newList);
    }
    public void remove(Renderable renderable) {
        renderables.remove(renderable);
    }

    protected void renderRenderable(Renderable renderable){
        sketch.scale(globalScale);
        renderable.render();
        renderable.renderText();
    }

    public void render() {
        for (Renderable renderable : renderables) {
            sketch.pushMatrix();
            renderRenderable(renderable);
            sketch.popMatrix();
        }
    }

    public void renderText() {
    }

}
