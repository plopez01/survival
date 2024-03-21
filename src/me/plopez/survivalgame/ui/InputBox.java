package me.plopez.survivalgame.ui;

import processing.core.PVector;

import java.util.function.Function;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PConstants.*;

public class InputBox extends UIElement {
    StringBuilder value = new StringBuilder();

    private int backgroundColor = sketch.color(255);
    private int foregroundColor = sketch.color(0);
    private int strokeColor = sketch.color(0);
    private float strokeWeight = 1;

    // Screen relative
    private final float caretWidth = 0.002f;
    // InputBox relative
    private final float caretHeight = 0.8f;
    private int caretPos = 0;

    private final float margin = 0.005f;

    private Runnable onSubmitRunnable;

    InputBox(PVector position, PVector size){
        super(position, size);
    }

    public String getValue(){
        return value.toString();
    }

    public void setValue(String value){
        this.value = new StringBuilder(value);
        caretPos = value.length();
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
        sketch.textSize(screenSize.y * caretHeight);
        sketch.text(getValue(), screenPos.x - screenSize.x/2 + margin*sketch.width, screenPos.y + sketch.textAscent()/2);

        // Caret
        drawCaret(screenPos, screenSize, 1000);
        sketch.rectMode(CORNER);
    }

    @Override
    public void onKeyPressed(char key) {
        super.onKeyPressed(key);

        if (handleSpecialKey(key)) return;
        if (sketch.textWidth(value.toString() + key)/sketch.height > size.x - margin*2) return;

        writeAtCaret(key);
    }

    private boolean handleSpecialKey(char key) {
        boolean handled = true;

        switch (key){
            case '\b' -> {
                if (!value.isEmpty()) {
                    eraseAtCaret(1);
                }
            }
            case '\n' -> submit();
            case ' ' -> handled = false;
            //DEL
            case 127 -> {
                eraseAtCaret(-1);
            }
            case CODED -> {
                switch (sketch.keyCode) {
                    case LEFT -> caretPos = Math.max(caretPos - 1, 0);
                    case RIGHT -> caretPos = Math.min(caretPos + 1, value.length());
                }
            }
            default -> {
                if (Character.isLetterOrDigit(key)) handled = false;
            }
        }

        return handled;
    }

    public void onSubmitHandler(Runnable runnable){
        onSubmitRunnable = runnable;
    }

    public void submit(){
        if (onSubmitRunnable != null) onSubmitRunnable.run();
    }

    public void writeAtCaret(String string){
        value.insert(caretPos, string);
        caretPos += string.length();
    }

    public void writeAtCaret(char character){
        value.insert(caretPos, character);
        caretPos++;
    }

    public void eraseAtCaret(int amount) {
        if (value.isEmpty()) return;

        if (amount > 0){
            if (amount > caretPos) amount = caretPos;

            value.delete(caretPos-amount, caretPos);
            caretPos -= amount;
        } else if (amount < 0) {
            amount = Math.abs(amount);
            if (amount > value.length() - caretPos ) amount = value.length();

            value.delete(caretPos, caretPos+amount);
        }

    }

    private void drawCaret(PVector screenPos, PVector screenSize, int blinkInterval){
        if (sketch.millis() % blinkInterval < blinkInterval/2) {
            sketch.rect(screenPos.x - screenSize.x/2 + margin*sketch.width + sketch.textWidth(value.substring(0, caretPos)),
                    screenPos.y,
                    caretWidth*sketch.width,
                    caretHeight*screenSize.y);
        }
    }
}
