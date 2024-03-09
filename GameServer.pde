Logger slog = new Logger(LOGGING_LEVEL.ALL, "SERVER> ");

class GameServer extends Server {
  int seed;
  int port;
  
  GameServer(PApplet parent, int port, int seed){
    super(parent, port);
    this.seed = seed;
    this.port = port;
  }

}

void serverEvent(Server someServer, Client client) {
  GameServer server = (GameServer) someServer;
  
  slog.info("We have a new client: " + client.ip());
  
  try {
    var packet = new ConnectPacket(server.seed);
    
    server.write(packet.serialize());
  
  } catch (IOException e) {
    log.error(e);
    e.printStackTrace();
  }
}
