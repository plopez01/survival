package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.rendering.Renderable;
import processing.core.PVector;

public class UI {
    public static Renderable connectUI(){
        InputBox inputBox = new InputBox(new PVector(100, 100), new PVector(100, 100));
        return inputBox;
    }
}
