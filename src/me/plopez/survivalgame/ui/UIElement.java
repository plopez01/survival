package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.util.Vector;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.focusedElement;
import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public abstract class UIElement implements Renderable {
    PVector position;
    PVector size;
    VerticalAlignment verticalAlignment = VerticalAlignment.Top;
    HorizontalAlignment horizontalAlignment = HorizontalAlignment.Left;
    int drawOrigin = CORNER;

    UIElement(PVector position, PVector size){
        this.position = position;
        this.size = size;
    }

    UIElement(PVector position, PVector size, int drawOrigin){
        this.position = position;
        this.size = size;
        this.drawOrigin = drawOrigin;
    }

    public void setVerticalAlignment(VerticalAlignment alignment) {
        verticalAlignment = alignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment alignment) {
        horizontalAlignment = alignment;
    }

    protected abstract void renderElement(PVector screenPos, PVector screenSize);

    public PVector getScreenPosition(){
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

        return elementPosition;
    }

    public PVector getScreenSize(){
        return PVector.mult(size, sketch.height);
    }

    public boolean hitTest(PVector screenPos) {
        return switch (drawOrigin){
            case CORNER -> Vector.boxCast(screenPos, getScreenPosition(), getScreenSize());
            case CENTER -> Vector.boxCast(screenPos, PVector.sub(getScreenPosition(), PVector.div(getScreenSize(), 2)), getScreenSize());
            default -> false;
        };
    }

    public final void render() {
        sketch.rectMode(drawOrigin);
        renderElement(getScreenPosition(), getScreenSize());
    }
    public final void renderText() {}

    public static boolean isFocused(UIElement element){
        return focusedElement == element && sketch.focused;
    }

    public void onClick(PVector position){}
    public void onMousePressed(PVector position){}
    public void onMouseReleased(PVector position){}
    public void onMouseDragged(PVector displacement){}

    public void onKeyPressed(char key){}
    public void onKeyReleased(char key){}
}
