Terrain terrain;
Debug debug = new Debug();

void setup() {
  fullScreen();
  background(0);
  terrain = new Terrain(null, 10);
  stroke(255, 20);
}

void draw() {
  terrain.drawAt(new PVector(0/2, 0/2), 5);
  terrain.noiseScale = ((float)mouseY/height)*100;
  debug.draw();
}
