Terrain terrain;

void setup() {
  fullScreen();
  background(0);
  terrain = new Terrain(null, 10);
}

void draw() {
  terrain.renderAt(new PVector(0, 0));
}