class GameServer extends Server {
  GameServer(PApplet parent, int port){
    super(parent, port);
   
}

void serverEvent(Server someServer, Client someClient) {
  println("We have a new client: " + someClient.ip());
}
