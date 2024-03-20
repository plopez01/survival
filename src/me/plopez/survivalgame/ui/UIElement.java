package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CORNER;

public abstract class UIElement implements Renderable {
    PVector position;
    PVector size;
    VerticalAlignment verticalAlignment = VerticalAlignment.Top;
    HorizontalAlignment horizontalAlignment = HorizontalAlignment.Left;

    UIElement(PVector position, PVector size){
        this.position = position;
        this.size = size;
    }

    public void setVerticalAlignment(VerticalAlignment alignment) {
        verticalAlignment = alignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment alignment) {
        horizontalAlignment = alignment;
    }

    protected abstract void renderElement(PVector position, PVector size);

    public final void render() {
        PVector elementPosition = new PVector();

        elementPosition.x = switch (horizontalAlignment) {
            case Left -> position.x*sketch.width;
            case Center -> position.x*sketch.width/2f + sketch.width/2f;
            case Right -> (1-position.x) * sketch.width;
        };

        elementPosition.y = switch (verticalAlignment) {
            case Top -> position.y*sketch.height;
            case Center -> position.y*sketch.height/2f + sketch.height/2f;
            case Bottom -> (1-position.y) * sketch.height;
        };

        renderElement(elementPosition, PVector.mult(size, sketch.height));
    }
    public final void renderText() {}

    public void onClick(PVector position){}
    public void onMousePressed(PVector position){}
    public void onMouseReleased(PVector position){}
    public void onMouseDragged(PVector displacement){}

    public void onKeyPressed(char key){}
    public void onKeyReleased(char key){}
}
