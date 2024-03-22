package me.plopez.survivalgame.objects;

import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.util.RangeConstrain;
import me.plopez.survivalgame.util.Vector;
import processing.core.PVector;
import processing.core.PApplet;

import static me.plopez.survivalgame.Globals.sketch;

public class Camera extends WorldObject {
    float zoom, resolution;
    float moveSensitivity, zoomSensitivity;

    RangeConstrain zoomConstrain;

    public Camera(float resolution, float zoom, RangeConstrain zoomConstrain, float moveSensitivity, float zoomSensitivity) {
        this.resolution = resolution;
        this.zoom = zoom;

        this.zoomConstrain = zoomConstrain;

        this.moveSensitivity = moveSensitivity;
        this.zoomSensitivity = zoomSensitivity;
    }

    public float getZoom() {
        return zoom;
    }

    void setZoom(float zoom) {
        this.zoom = zoomConstrain.enforce(zoom);
    }

    public float getResolution() {
        return resolution;
    }

    public float getZoomPosition() {
        return zoomConstrain.getPosition(zoom);
    }

    public void updateZoom(float change) {
        float newZoom = zoom + change * zoomSensitivity;

        if (zoomConstrain.inBounds(newZoom)) {
            PVector mouseTarget = getWorldMouse();

            zoom = newZoom;

            PVector newMouseTarget = getWorldMouse();

            transform.add(mouseTarget.sub(newMouseTarget));
        }
    }

    PVector getWorldMouse() {
        return Vector.multiplyVectors(Mouse.getMouseDistFromCenter(sketch), getWorldViewportSize());
    }

    public PVector getRelativeWorldMouse() {
        return Vector.multiplyVectors(Mouse.getMouseDistFromCenter(sketch), getWorldViewportSize()).add(transform);
    }

    PVector getWorldViewportSize() {
        return toWorldSpace(new PVector(sketch.width, sketch.height));
    }

    public PVector toScreenSpace(PVector in) {
        return new PVector(in.x * -zoom * resolution, in.y * -zoom * resolution, in.z * zoom * resolution);
    }

    public PVector toWorldSpace(PVector in) {
        return new PVector(in.x / (-zoom * resolution), in.y / (-zoom * resolution), in.z / (zoom * resolution));
    }

    public PVector toRelativeScreenSpace(PVector in) {
        PVector screenSpace = toScreenSpace(PVector.add(transform, in));
        return new PVector(screenSpace.x + ((float) sketch.width / 2), screenSpace.y + ((float) sketch.height / 2), screenSpace.z);
    }

    public void translate(PVector vec) {
        transform.add(vec);
    }
}
