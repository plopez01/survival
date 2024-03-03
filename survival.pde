Terrain terrain;
Camera camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);
Debug debug = new Debug();

Mouse mouse = new Mouse();

Renderer renderer = new Renderer(camera, 0.01);

Player player = new Player("Pau", 1000, color(random(255), random(255), random(255)));
void setup() {
  fullScreen();
  background(0);
  
  terrain = new Terrain(4, 1000, 0.5, 5, 5);
  player.translate(new PVector(0, 0));
  renderer.add(player);
  
  noiseSeed(5);
}

void draw() {
  terrain.renderAt(camera);
    
  renderer.render();
  
  pushMatrix();
  PVector transform = new PVector(10, 0, 0.01);
  PVector screenSpace = camera.toRelativeScreenSpace(transform);

  translate(screenSpace.x, screenSpace.y);
  scale(screenSpace.z);
  fill(255, 0, 0);
  square(transform.x, transform.y, transform.z);
  popMatrix();
  
  debug.add("FPS", frameRate);
  debug.add("Pos", camera.transform);
  debug.add("Zoom", camera.zoom);
  debug.add("ScreenMouse", mouse.getMouseDistFromCenter());
  debug.add("WorldMouse", camera.getRelativeWorldMouse(mouse));
  debug.showPointer();
  debug.render();
}
