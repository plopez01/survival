package me.plopez.survivalgame.util;

public class RangeConstrain {
    float min, max;

    public RangeConstrain(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float enforce(float value) {
        if (value > max) return max;
        else if (value < min) return min;
        else return value;
    }

    public int enforce(int value) {
        if (value > max) return (int) max;
        else if (value < min) return (int) min;
        else return value;
    }

    public boolean inBounds(float value) {
        return value <= max && value >= min;
    }

    public float getPosition(float value) {
        return (value - min) / (max - min);
    }
}
