package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.focusedElement;
import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.CENTER;

public class UI {
    public static Renderable connectUI(){
        InputBox inputBox = new InputBox(new PVector(0f, 0), new PVector(0.4f, 0.04f));
        inputBox.setHorizontalAlignment(HorizontalAlignment.Center);
        inputBox.setVerticalAlignment(VerticalAlignment.Center);
        focusedElement = inputBox;
        return inputBox;
    }
}
