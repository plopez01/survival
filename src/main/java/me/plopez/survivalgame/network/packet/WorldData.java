package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.rendering.World;
import me.plopez.survivalgame.server.GameServer;

import java.io.*;

public class WorldData extends NetworkPacket {
    public World world;

    public WorldData(World world) {
        this.world = world;
    }

    /** ----------------------------------------------------------------------
     * | Seed | Number of objects (int) | WorldObject 1 | WorldObject 2 ... |
     * ----------------------------------------------------------------------
     */
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(world);
    }

    @Override
    public void handleClient(GameClient client) {

    }

    @Override
    public void handleServer(GameServer server, Client client) {

    }
}
