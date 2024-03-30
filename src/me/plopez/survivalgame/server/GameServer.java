package me.plopez.survivalgame.server;

import me.plopez.survivalgame.objects.entity.Entity;
import me.plopez.survivalgame.objects.entity.Player;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.Server;
import me.plopez.survivalgame.network.packet.*;
import me.plopez.survivalgame.rendering.World;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;

public class GameServer extends Server {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.PURPLE + "SERVER> " + Logger.RESET);
    int port;
    World world;

    public GameServer(PApplet parent, int port, World world) {
        super(parent, port);
        this.port = port;
        this.world = world;
    }

    public int getSeed() {
        return world.getSeed();
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
                        ClientConnect clientConnect = (ClientConnect) inPacket;
                        try {
                            registerPlayer(clientConnect.player);
                            broadcast(inPacket);
                        } catch (DuplicatePlayerException e) {
                            log.warn("Client (" + client.ip() + ") tried to connect with name "
                                    + clientConnect.player.getName() +
                                    " but another player with the same name already exists.");
                            disconnect(client);
                        }
                    }
                    case MOVE_COMMAND -> {
                        MoveCommand moveCommand = (MoveCommand) inPacket;

                        Entity entity = world.getEntity(moveCommand.entityID);
                        entity.transform.set(new PVector(-moveCommand.target.x, -moveCommand.target.y, entity.transform.z));

                        broadcast(inPacket);
                    }
                    case CLIENT_DISCONNECT -> {
                        ClientDisconnect clientDisconnect = (ClientDisconnect) inPacket;

                        Player localPlayer = world.getPlayer(clientDisconnect.player.getName());
                        removePlayer(localPlayer);

                        disconnect(client);

                        echoToOthers(inPacket, client);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + inPacket);
                }

                client = available();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcast(NetworkPacket packet) throws IOException {
        for (Client c : clients) {
            if (c == null) continue;
            c.output.write(packet.serialize());
        }
    }

    private void echoToOthers(NetworkPacket packet, Client from) throws IOException {
        for (Client c : clients) {
            if (c == null || c == from) continue;
            c.output.write(packet.serialize());
        }
    }

    public void registerPlayer(Player player) throws DuplicatePlayerException {
        log.debug("Registering player " + player.getName());
        world.addPlayer(player);
    }

    public void removePlayer(Player player){
        log.debug("Removing player " + player.getName());
        world.removePlayer(player.getName());
    }

    @Override
    public void onClientConnect(Client client) {
        super.onClientConnect(client);
        log.info("We have a new client: " + client.ip());
        try {
            var packet = new WorldData(world);
            client.output.write(packet.serialize());
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    @Override
    public void onClientDisconnect(Client client) {
        super.onClientDisconnect(client);
        log.info("A client has disconnected");
    }
}

