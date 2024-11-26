package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Camera;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;

import static me.plopez.survivalgame.Globals.coordinateScalar;
import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PApplet.abs;

public class Terrain implements Serializable {
    int terrainSize, translationOffset;
    float heightOffset;

    int baseDetail, zoomDetail;

    public Terrain(int terrainSize, int translationOffset, float heightOffset, int baseDetail, int zoomDetail) {
        this.terrainSize = terrainSize;
        this.translationOffset = translationOffset;
        this.heightOffset = heightOffset;

        this.baseDetail = baseDetail;
        this.zoomDetail = zoomDetail;
    }

    public void renderAt(Camera cam) {
        sketch.stroke(255, 20);
        for (int xoff = 0; xoff < sketch.width / cam.getResolution(); xoff++) {
            for (int yoff = 0; yoff < sketch.height / cam.getResolution(); yoff++) {
                float x = (cam.transform.x * cam.getZoom() / coordinateScalar + xoff) - ((float) sketch.width / cam.getResolution() / 2);
                float y = (cam.transform.y * cam.getZoom() / coordinateScalar + yoff) - ((float) sketch.height / cam.getResolution() / 2);

                sketch.noiseDetail(PApplet.round(zoomDetail * cam.getZoomPosition()) + baseDetail);

                float terrainHeight = getNoiseHeight(x / cam.getZoom(), y / cam.getZoom());

                if (colorRegion(terrainHeight, sketch.color(0, 0, 255), sketch.color(70, 70, 255), 0, 0.3f)) ;
                else if (colorRegion(terrainHeight, sketch.color(70, 70, 255), sketch.color(200, 200, 100), 0.3f, 0.35f))
                    ;
                else if (colorRegion(terrainHeight, sketch.color(200, 200, 100), sketch.color(40, 120, 40), 0.35f, 0.6f))
                    ;
                else if (colorRegion(terrainHeight, sketch.color(40, 120, 40), sketch.color(80, 60, 60), 0.6f, 0.65f)) ;
                else if (colorRegion(terrainHeight, sketch.color(80, 60, 60), sketch.color(180), 0.65f, 0.8f)) ;
                else sketch.fill(255 * terrainHeight);

                sketch.rect(xoff * cam.getResolution(), yoff * cam.getResolution(), cam.getResolution(), cam.getResolution());
            }
        }
    }

    public float getHeight(float x, float y){
        return getNoiseHeight(x / coordinateScalar, y / coordinateScalar);
    }

    public float getHeight(PVector pos) {
        return getHeight(pos.x, pos.y);
    }

    private float getNoiseHeight(float x, float y){
        float distanceFromOrigin = (abs(x) + abs(y)) / terrainSize + heightOffset;
        return sketch.noise(x + translationOffset, y + translationOffset) / distanceFromOrigin;
    }

    // 0.3f in this function is the hardcoded sea value
    // 1 is the maxmimum value of the noise function
    public float getSurfaceRadius(){
        return terrainSize * (1/0.3f - heightOffset) * coordinateScalar;
    }

    boolean colorRegion(float terrainHeight, int cfrom, int cto, float from, float to) {
        if (terrainHeight >= from && terrainHeight < to) {
            sketch.fill(sketch.lerpColor(cfrom, cto, (terrainHeight - from) / (to - from)));
            return true;
        } else return false;
    }
}