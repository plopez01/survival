package me.plopez.survivalgame.network.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class PacketInputStream extends ObjectInputStream {
    public PacketInputStream(InputStream in) throws IOException {
        super(in);
    }

    public NetworkPacket readPacket() throws IOException{
        try {
            return (NetworkPacket) readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
