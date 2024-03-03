Terrain terrain;
Camera camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);
Debug debug = new Debug();

Mouse mouse = new Mouse();

Renderer renderer = new Renderer(camera);

Player player = new Player("Pau", 2, color(random(255), random(255), random(255)));
void setup() {
  fullScreen();
  background(0);
  
  terrain = new Terrain(4, 1000, 0.5, 5, 5);
  renderer.add(player);
}

void draw() {
  terrain.renderAt(camera);
    
  renderer.render();
  
  debug.add("FPS", frameRate);
  debug.add("Pos", camera.transform);
  debug.add("Zoom", camera.zoom);
  debug.add("ScreenMouse", mouse.getMouseDistFromCenter());
  debug.add("WorldMouse", camera.getWorldMouse(mouse));
  //debug.showPointer();
  debug.render();
}
