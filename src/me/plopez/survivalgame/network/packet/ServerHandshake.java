package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.network.packet.NetworkPacket;
import me.plopez.survivalgame.network.packet.PacketType;

import java.io.*;

public class ServerHandshake extends NetworkPacket {
    public int seed;

    public ServerHandshake(int seed) {
        super(PacketType.SERVER_HANDSHAKE);
        this.seed = seed;
    }

    public ServerHandshake(InputStream is) throws IOException {
        super(is);
    }

    protected void readFrom(ObjectInputStream stream) throws IOException {
        seed = stream.readInt();
    }

    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeInt(seed);
    }
}
