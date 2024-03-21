package me.plopez.survivalgame.ui;

import me.plopez.survivalgame.util.RangeConstrain;
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

    private long caretBlinkTime = 0;

    InputBox(PVector position, PVector size){
        super(position, size);
    }

    InputBox(PVector position, PVector size, int drawOrigin){
        super(position, size, drawOrigin);
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

        sketch.fill(backgroundColor);
        sketch.rect(screenPos.x, screenPos.y, screenSize.x, screenSize.y);

        sketch.noStroke();

        sketch.fill(foregroundColor);
        sketch.textSize(screenSize.y * caretHeight);
        sketch.text(getValue(), screenPos.x - screenSize.x/2 + margin*sketch.width, screenPos.y + sketch.textAscent()/2);

        // Caret
        if (isFocused(this)) drawCaret(screenPos, screenSize, 1000);
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
                    case LEFT -> moveCaret(-1);
                    case RIGHT -> moveCaret(1);
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

    public void moveCaret(int amount) {
        RangeConstrain constrain = new RangeConstrain(0, value.length());

        caretPos += amount;

        if (constrain.inBounds(caretPos)) caretBlinkTime = 0;
        caretPos = constrain.enforce(caretPos);
    }

    public void writeAtCaret(String string){
        value.insert(caretPos, string);
        caretPos += string.length();
        caretBlinkTime = 0;
    }

    public void writeAtCaret(char character){
        writeAtCaret(Character.toString(character));
    }

    public void eraseAtCaret(int amount) {
        if (value.isEmpty()) return;

        if (amount > 0){
            if (amount > caretPos) amount = caretPos;

            value.delete(caretPos-amount, caretPos);
            caretPos -= amount;
        } else if (amount < 0) {
            if (caretPos == value.length()) return;

            amount = Math.abs(amount);
            if (amount > value.length() - caretPos ) amount = value.length();

            value.delete(caretPos, caretPos+amount);
        }

        caretBlinkTime = 0;
    }

    private void drawCaret(PVector screenPos, PVector screenSize, int blinkInterval){
        if (caretBlinkTime % blinkInterval < blinkInterval/2) {
            sketch.rect(screenPos.x - screenSize.x/2 + margin*sketch.width + sketch.textWidth(value.substring(0, caretPos)),
                    screenPos.y,
                    caretWidth*sketch.width,
                    caretHeight*screenSize.y);
        }
        caretBlinkTime += (long) (1000/sketch.frameRate);
    }
}
