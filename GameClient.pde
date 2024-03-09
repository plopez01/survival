import java.nio.ByteBuffer;

class GameClient extends Client {
  Terrain terrain = new Terrain(4, 5000, 0.5, 5, 5);
  Camera camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);
  Renderer renderer = new Renderer(camera, 0.01);
  
  GameClient(PApplet parent, String adress, int port){
    super(parent, adress, port);
    
    try {
      ServerHandshake handshake = new ServerHandshake(input);
      seedManager.setSeed(handshake.seed);
    } catch (IOException e) {
      log.error(e);
      e.printStackTrace();
    }
  }
  
  void tick() {
    terrain.renderAt(camera);
    
    renderer.render();
  }
  
  int readInt() {
    while (available() < 4);
    ByteBuffer wrapped = ByteBuffer.wrap(readBytes(4));
    return wrapped.getInt();
  }
  
}
