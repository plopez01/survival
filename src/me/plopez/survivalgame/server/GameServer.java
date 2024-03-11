package me.plopez.survivalgame.server;

import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.Server;
import me.plopez.survivalgame.network.packet.ServerHandshake;
import processing.core.PApplet;

import java.io.IOException;

public class GameServer extends Server {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.PURPLE + "SERVER> " + Logger.RESET);
    int seed;
    int port;
    int players = 0;

    public GameServer(PApplet parent, int port, int seed) {
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

    @Override
    public void onClientConnect(Server someServer, Client client) {
        GameServer server = (GameServer) someServer;

        server.log.info("We have a new client: " + client.ip());

        try {
            var packet = new ServerHandshake(server.getSeed());

            server.write(packet.serialize());

        } catch (IOException e) {
            server.log.error(e);
            e.printStackTrace();
        }
    }
}

