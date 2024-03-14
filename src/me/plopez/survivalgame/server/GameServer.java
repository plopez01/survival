package me.plopez.survivalgame.server;

import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.exception.PlayerExistsException;
import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.Server;
import me.plopez.survivalgame.network.packet.*;
import me.plopez.survivalgame.objects.WorldObject;
import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class GameServer extends Server {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.PURPLE + "SERVER> " + Logger.RESET);
    int seed;
    int port;
    List<WorldObject> worldObjects;

    Map<String, Player> players = new HashMap<>();

    public GameServer(PApplet parent, int port, int seed) {
        super(parent, port);
        this.seed = seed;
        this.port = port;
        this.worldObjects = new ArrayList<>();
    }

    public int getSeed() {
        return seed;
    }

    public int getPort() {
        return port;
    }

    public void tick() {
        try {
            Client client = available();
            while (client != null) {
                PacketInputStream is = new PacketInputStream(client.input);
                NetworkPacket inPacket = PacketType.getType(is.readByte()).makePacket(is);

                log.debug("Incoming packet " + inPacket.getType());

                switch (inPacket.getType()){
                    case CLIENT_CONNECT -> {
                        for (Client c : clients) {
                            if (c == null) continue;
                            c.write(inPacket.serialize());

                            ClientConnect clientConnect = (ClientConnect) inPacket;
                            try {
                                registerPlayer(clientConnect.player);
                            } catch (PlayerExistsException e) {
                                log.warn("Client (" + client.ip() + ") tried to connect with name "
                                                + clientConnect.player.getName() +
                                                " but another player with the same name already exists.");
                            }
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + inPacket);
                }

                client = available();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerPlayer(Player player) throws PlayerExistsException {
        if (players.containsKey(player.getName())) throw new PlayerExistsException();
        worldObjects.add(player);
        players.put(player.getName(), player);
    }

    @Override
    public void onClientConnect(Client client) {
        super.onClientConnect(client);
        log.info("We have a new client: " + client.ip());
        try {
            var packet = new ServerHandshake(getSeed(), worldObjects);
            client.output.write(packet.serialize());
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
}

