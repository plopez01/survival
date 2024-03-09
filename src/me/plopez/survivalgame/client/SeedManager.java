package me.plopez.survivalgame.client;

import processing.core.PApplet;

public class SeedManager {
    PApplet sketch;
    int seed;

    public SeedManager(PApplet sketch) {
        this.sketch = sketch;
        generateSeed();
    }

    SeedManager(PApplet sketch, int seed) {
        this.sketch = sketch;
        setSeed(seed);
    }

    public void setSeed(int newSeed) {
        seed = newSeed;
        sketch.noiseSeed(newSeed);
        sketch.randomSeed(newSeed);
    }

    public int getSeed() {
        return seed;
    }

    void generateSeed() {
        setSeed(PApplet.parseInt(sketch.random(Integer.MIN_VALUE, Integer.MAX_VALUE)));
    }
}
