package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.util.Vector;
import me.plopez.survivalgame.vector.VectorF;

import java.util.ArrayList;

import static me.plopez.survivalgame.Globals.focusedElement;

//TODO: this might be able to extend a UIElement itself, how should we handle focused Canvases?
public class Canvas implements Renderable {
    private final ArrayList<UIElement> elements = new ArrayList<>();

    public void addElement(UIElement element){
        elements.add(element);
    }

    public void onClick(VectorF mousePos){
        for (var uiElement : elements) {
            if (uiElement.hitTest(mousePos)) {
                focusedElement = uiElement;
                uiElement.onClick(VectorF.sub(mousePos, uiElement.getScreenPosition()));
                return;
            }
        }
    }

    @Override
    public void render() {
        for (var uiElement : elements) {
            uiElement.render();
        }
    }

    @Override
    public void renderText() {
        for (var uiElement : elements) {
            uiElement.renderText();
        }
    }
}
