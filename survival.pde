SeedManager seedManager = new SeedManager();

Debug debug = new Debug();
Logger log = new Logger(LOGGING_LEVEL.ALL, "CLIENT> ");

Mouse mouse = new Mouse();

Player player = new Player("Pau", 10000, color(random(255), random(255), random(255)));

boolean host = false;
GameServer server;
GameClient client;

void setup() {
  log.info("Starting game.");
  //fullScreen();
  size(640, 480);
  
  background(0);
  
  if (host) {
    server = new GameServer(this, 5000, seedManager.seed);
    log.info("Server started at port " + server.port);
  }
  
  client = new GameClient(this, "127.0.0.1", 5000);
  log.info("Connected to server!");
  
  log.info("World seed: " + seedManager.seed);
}

void draw() {
  client.tick();
  
  debug.add("FPS", frameRate);
  debug.add("Game seed", seedManager.seed);
  debug.add("Pos", client.camera.transform);
  debug.add("Zoom", client.camera.zoom);
  debug.add("ScreenMouse", mouse.getMouseDistFromCenter());
  debug.add("WorldMouse", client.camera.getRelativeWorldMouse(mouse));
  //debug.showPointer();
  debug.render();
}
