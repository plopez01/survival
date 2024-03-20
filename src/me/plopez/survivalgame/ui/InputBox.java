package me.plopez.survivalgame.ui;

import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;

public class InputBox extends UIElement {

    InputBox(PVector position, PVector size){
        super(position, size);
    }

    @Override
    protected void renderElement(PVector position, PVector size) {
        sketch.fill(255);
        sketch.rect(position.x, position.y, size.x, size.y);
    }
}
