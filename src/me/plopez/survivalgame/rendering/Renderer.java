package me.plopez.survivalgame.rendering;

import java.util.ArrayList;
import java.util.List;

import static me.plopez.survivalgame.Globals.sketch;

public class Renderer implements Renderable {
    ArrayList<Renderable> renderableBuffer = new ArrayList<>();
    float globalScale;

    public Renderer(float globalScale) {
        this.globalScale = globalScale;
    }

    public void add(Renderable renderable) {
        renderableBuffer.add(renderable);
    }
    public void add(List<Renderable> newList) {
        renderableBuffer.addAll(newList);
    }
    public void remove(Renderable renderable) {
        renderableBuffer.remove(renderable);
    }

    protected void renderRenderable(Renderable renderable){
        sketch.scale(globalScale);
        renderable.render();
        renderable.renderText();
    }

    public void render() {
        render(renderableBuffer);
    }

    public void render(List<Renderable> renderables) {
        for (Renderable renderable : renderables) {
            sketch.pushMatrix();
            renderRenderable(renderable);
            sketch.popMatrix();
        }
    }

    public void renderText() {
    }

}
