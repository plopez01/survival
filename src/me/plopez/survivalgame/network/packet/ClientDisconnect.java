package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.objects.entity.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientDisconnect extends NetworkPacket {
    public Player player;

    public ClientDisconnect(Player player) {
        super(PacketType.CLIENT_DISCONNECT);
        this.player = player;
    }

    public ClientDisconnect(PacketInputStream pis) throws IOException {
        super(PacketType.CLIENT_DISCONNECT);
        player = (Player) pis.readPackedObject();
    }

    @Override
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(player);
    }
}
