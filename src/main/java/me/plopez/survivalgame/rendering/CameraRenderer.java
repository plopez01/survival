package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.objects.WorldObject;
import processing.core.PApplet;
import processing.core.PVector;

import static me.plopez.survivalgame.Globals.sketch;

public class CameraRenderer extends Renderer {
    Camera cam;
    public CameraRenderer(float globalScale, Camera cam) {
        super(globalScale);
        this.cam = cam;
    }

    public Camera getCam() {
        return cam;
    }

    @Override
    protected void renderRenderable(Renderable renderable) {
        if (renderable instanceof WorldObject worldObject) {

            PVector screenSpace = cam.toRelativeScreenSpace(worldObject.transform);

            sketch.strokeWeight(sketch.g.strokeWeight / screenSpace.z);
            sketch.translate(screenSpace.x, screenSpace.y);


            sketch.pushMatrix();
            sketch.scale(screenSpace.z * globalScale / sketch.g.textSize);
            renderable.renderText();
            sketch.popMatrix();

            sketch.scale(screenSpace.z * globalScale);

            renderable.render();

        } else {
            renderable.render();
            renderable.renderText();
            throw new RuntimeException("Trying to render a non worldobject type with a camera render, this is fine");
        }
    }
}
