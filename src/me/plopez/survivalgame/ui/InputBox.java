package me.plopez.survivalgame.ui;

import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;

public class InputBox extends UIElement {

    InputBox(PVector position, PVector size){
        this.position = position;
        this.size = size;
    }

    @Override
    public void render(){
        sketch.fill(255);
        sketch.rect(position.x, position.y, size.x, size.y);
    }

}
