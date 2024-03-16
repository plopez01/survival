package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.entities.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientConnect extends NetworkPacket {
    public Player player;

    public ClientConnect(Player player) {
        super(PacketType.CLIENT_CONNECT);
        this.player = player;
    }

    public ClientConnect(PacketInputStream pis) throws IOException {
        super(PacketType.CLIENT_CONNECT);
        player = (Player) pis.readPackedObject();
    }

    @Override
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(player);
    }
}
