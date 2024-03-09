package me.plopez.survivalgame.network.packet;

import java.io.*;

public abstract class NetworkPacket {
    PacketType type;

    protected NetworkPacket(PacketType type) {
        this.type = type;
    }

    protected NetworkPacket(InputStream is) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(is);
        type = PacketType.values()[ois.readByte()];
        readFrom(ois);
    }

    protected abstract void writeTo(ObjectOutputStream stream) throws IOException;

    protected abstract void readFrom(ObjectInputStream stream) throws IOException;

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeByte((byte) type.ordinal());
            writeTo(os);
        }

        return baos.toByteArray();
    }

}