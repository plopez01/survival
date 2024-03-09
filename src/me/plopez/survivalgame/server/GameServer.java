package me.plopez.survivalgame.server;

import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Server;
import processing.core.PApplet;

public class GameServer extends Server {
  public Logger log = new Logger(LoggingLevel.ALL, Logger.PURPLE + "SERVER> " + Logger.RESET);
  int seed;
  int port;
  int players = 0;

  public GameServer(PApplet parent, int port, int seed){
    super(parent, port);
    this.seed = seed;
    this.port = port;
  }
  public int getSeed() {
    return seed;
  }

  public int getPort() {
    return port;
  }
}

