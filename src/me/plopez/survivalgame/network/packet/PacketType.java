package me.plopez.survivalgame.network.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.Function;

public enum PacketType {
    SERVER_HANDSHAKE(ServerHandshake::new),
    CLIENT_HANDSHAKE(null);

    final Translator translator;

    PacketType(Translator translator) {
        this.translator = translator;
    }

    public NetworkPacket makePacket(ObjectInputStream ois) throws IOException {
        return translator.translate(ois);
    }

    public static PacketType getType(int index){
        return PacketType.values()[index];
    }

    interface Translator {
        NetworkPacket translate(ObjectInputStream ois) throws IOException;
    }
}
