package me.plopez.survivalgame.objects;

import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.util.RangeConstrain;
import me.plopez.survivalgame.util.Vector;
import me.plopez.survivalgame.vector.VectorF;
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
            VectorF mouseTarget = getWorldMouse();

            zoom = newZoom;

            VectorF newMouseTarget = getWorldMouse();

            transform.add(mouseTarget.sub(newMouseTarget));
        }
    }

    VectorF getWorldMouse() {
        return Vector.multiplyVectors(Mouse.getMouseDistFromCenter(sketch), getWorldViewportSize());
    }

    public VectorF getRelativeWorldMouse() {
        return getWorldMouse().add(transform);
    }

    VectorF getWorldViewportSize() {
        return toWorldSpace(new VectorF(sketch.width, sketch.height));
    }

    public VectorF toScreenSpace(VectorF in) {
        return new VectorF(in.x * zoom * resolution, in.y * zoom * resolution, in.z * zoom * resolution);
    }

    public VectorF toWorldSpace(VectorF in) {
        return new VectorF(in.x / (zoom * resolution), in.y / (zoom * resolution), in.z / (zoom * resolution));
    }

    public VectorF toRelativeScreenSpace(VectorF in) {
        VectorF newPos = new VectorF(-transform.x + in.x, -transform.y + in.y, transform.z + in.z);
        VectorF screenSpace = toScreenSpace(newPos);
        return new VectorF(screenSpace.x + ((float) sketch.width / 2), screenSpace.y + ((float) sketch.height / 2), screenSpace.z);
    }

    public void translate(VectorF vec) {
        transform.add(vec);
    }
}
