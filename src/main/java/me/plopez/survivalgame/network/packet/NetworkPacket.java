package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.server.GameServer;

import java.io.*;

public abstract class NetworkPacket implements Serializable {

    public abstract void handleClient(GameClient client);
    public abstract void handleServer(GameServer server, Client client);

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(this);
        }

        return baos.toByteArray();
    }
}