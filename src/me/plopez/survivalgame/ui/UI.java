package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PVector;

public class UI {
    public static Renderable connectUI(){
        InputBox inputBox = new InputBox(new PVector(0f, 0), new PVector(0.1f, 0.1f));
        inputBox.setHorizontalAlignment(HorizontalAlignment.Center);
        inputBox.setVerticalAlignment(VerticalAlignment.Center);
        return inputBox;
    }
}
