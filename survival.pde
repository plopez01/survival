Terrain terrain;
Camera camera = new Camera(10, 1, 4);
Debug debug = new Debug();

void setup() {
  fullScreen();
  background(0);
  terrain = new Terrain(null, 10);
  stroke(255, 20);
}

void draw() {
  terrain.drawAt(camera);
  debug.draw();
}
