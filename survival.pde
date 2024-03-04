import processing.net.*;

Debug debug = new Debug();
Logger log = new Logger(LOGGING_LEVEL.ALL);

Terrain terrain;
Camera camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);

Mouse mouse = new Mouse();

Renderer renderer = new Renderer(camera, 0.01);

Player player = new Player("Pau", 10000, color(random(255), random(255), random(255)));
void setup() {
  log.info("Starting game.");
  fullScreen();
  background(0);
  
  terrain = new Terrain(4, 5000, 0.5, 5, 5);
  log.info("World seed: " + terrain.getSeed());
    
    
  player.translate(new PVector(0, 0));
  renderer.add(player);
}

void draw() {
  terrain.renderAt(camera);
    
  renderer.render();
  
  debug.add("FPS", frameRate);
  debug.add("Pos", camera.transform);
  debug.add("Zoom", camera.zoom);
  debug.add("ScreenMouse", mouse.getMouseDistFromCenter());
  debug.add("WorldMouse", camera.getRelativeWorldMouse(mouse));
  //debug.showPointer();
  debug.render();
}
