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

    public World getWorld() {
        return world;
    }

    public void tick() {
        try {
            Client client = available();
            while (client != null) {
                PacketInputStream is = new PacketInputStream(client.input);
                NetworkPacket inPacket = is.readPacket();

                log.debug("Incoming packet " + inPacket.getClass().getName());

                inPacket.handleServer(this, client);

                if (inPacket instanceof BroadcastPacket) broadcast(inPacket);
                if (inPacket instanceof ForwardPacket) echoToOthers(inPacket, client);

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

