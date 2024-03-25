package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.util.Vector;
import me.plopez.survivalgame.vector.VectorF;

import static me.plopez.survivalgame.Globals.focusedElement;
import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public abstract class UIElement implements Renderable {
    VectorF position;
    VectorF size;
    VerticalAlignment verticalAlignment = VerticalAlignment.Top;
    HorizontalAlignment horizontalAlignment = HorizontalAlignment.Left;
    int drawOrigin = CORNER;

    UIElement(VectorF position, VectorF size){
        this.position = position;
        this.size = size;
    }

    UIElement(VectorF position, VectorF size, int drawOrigin){
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

    protected abstract void renderElement(VectorF screenPos, VectorF screenSize);

    public VectorF getScreenPosition(){
        VectorF elementPosition = new VectorF();

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

    public VectorF getScreenSize(){
        return VectorF.mult(size, sketch.height);
    }

    public boolean hitTest(VectorF screenPos) {
        return switch (drawOrigin){
            case CORNER -> Vector.boxCast(screenPos, getScreenPosition(), getScreenSize());
            case CENTER -> Vector.boxCast(screenPos, VectorF.sub(getScreenPosition(), VectorF.div(getScreenSize(), 2)), getScreenSize());
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

    public void onClick(VectorF position){}
    public void onMousePressed(VectorF position){}
    public void onMouseReleased(VectorF position){}
    public void onMouseDragged(VectorF displacement){}

    public void onKeyPressed(char key){}
    public void onKeyReleased(char key){}
}
