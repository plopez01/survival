package me.plopez.survivalgame.ui;

import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.*;

public class InputBox extends UIElement {
    String value = "";

    int backgroundColor = sketch.color(255);
    int foregroundColor = sketch.color(0);
    int strokeColor = sketch.color(0);
    float strokeWeight = 1;

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

    public void setBackgroundColor(int color){
        backgroundColor = color;
    }

    public void setForegroundColor(int color){
        foregroundColor = color;
    }

    public void setStrokeColor(int color){
        strokeColor = color;
    }

    public void setStrokeWeight(float weight){
        strokeWeight = weight;
    }

    @Override
    protected void renderElement(PVector screenPos, PVector screenSize) {
        sketch.stroke(strokeColor);
        sketch.strokeWeight(strokeWeight);

        sketch.rectMode(CENTER);
        sketch.fill(backgroundColor);
        sketch.rect(screenPos.x, screenPos.y, screenSize.x, screenSize.y);

        sketch.noStroke();

        sketch.fill(foregroundColor);
        sketch.text(value, screenPos.x - screenSize.x/2 + margin*sketch.width, screenPos.y + sketch.textAscent()/2);
        sketch.rectMode(CORNER);

        // Caret
        //sketch.square(screenPos.x + sketch.textWidth(value) - screenSize.x/2, screenPos.y, 20);
    }

    @Override
    public void onKeyPressed(char key) {
        super.onKeyPressed(key);

        if (handleSpecialKey(key)) return;
        if (sketch.textWidth(value + key)/sketch.height > size.x - margin*2) return;

        value += key;
    }

    private boolean handleSpecialKey(char key) {
        boolean handled = true;

        switch (key){
            case '\b' -> {
                if (!value.isEmpty()) value = value.substring(0, value.length()-1);
            }
            case '\n', CODED -> {}
            default -> handled = false;
        }

        return handled;
    }
}
