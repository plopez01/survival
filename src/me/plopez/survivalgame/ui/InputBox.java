package me.plopez.survivalgame.ui;

import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public class InputBox extends UIElement {
    String value = "";

    int backgroundColor = sketch.color(255);
    int foregroundColor = sketch.color(0);
    float margin = 0.005f;

    InputBox(PVector position, PVector size){
        super(position, size);
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    @Override
    protected void renderElement(PVector position, PVector size) {
        sketch.rectMode(CENTER);
        sketch.fill(backgroundColor);
        sketch.rect(position.x, position.y, size.x, size.y);

        sketch.fill(foregroundColor);
        sketch.text(value, position.x - size.x/2 + margin*sketch.width, position.y + sketch.textAscent()/2);
        sketch.rectMode(CORNER);

        //TODO: fix this, max write length does not work
        sketch.box(position.x + sketch.textWidth(value)/sketch.width, position.y, 20);
    }

    @Override
    public void onKeyPressed(char key) {
        super.onKeyPressed(key);

        if (handleSpecialKey(key)) return;
        if (sketch.textWidth(value + key)/sketch.width > size.x - margin*2) return;

        value += key;
    }

    private boolean handleSpecialKey(char key) {
        if (key == '\b'){
            if (!value.isEmpty()) value = value.substring(0, value.length()-1);
            return true;
        }

        return false;
    }
}
