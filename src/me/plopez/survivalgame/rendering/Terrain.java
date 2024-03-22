package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Camera;
import processing.core.PApplet;

import static me.plopez.survivalgame.Globals.sketch;
import static processing.core.PApplet.abs;

public class Terrain {
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
                float x = (cam.transform.x * cam.getZoom() + xoff) - ((float) sketch.width / cam.getResolution() / 2);
                float y = (cam.transform.y * cam.getZoom() + yoff) - ((float) sketch.height / cam.getResolution() / 2);

                sketch.noiseDetail(PApplet.round(zoomDetail * cam.getZoomPosition()) + baseDetail);

                float distanceFromOrigin = (abs(x / cam.getZoom()) + abs(y / cam.getZoom())) / terrainSize + heightOffset;
                float terrainHeight = sketch.noise(x / cam.getZoom() + translationOffset, y / cam.getZoom() + translationOffset) / distanceFromOrigin;

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

    boolean colorRegion(float terrainHeight, int cfrom, int cto, float from, float to) {
        if (terrainHeight >= from && terrainHeight < to) {
            sketch.fill(sketch.lerpColor(cfrom, cto, (terrainHeight - from) / (to - from)));
            return true;
        } else return false;
    }
}