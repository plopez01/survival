package me.plopez.survivalgame.network.packet;

import java.io.IOException;

public enum PacketType {
    SERVER_HANDSHAKE(ServerHandshake::new),
    CLIENT_CONNECT(ClientConnect::new),
    MOVE_COMMAND(MoveCommand::new);

    final Translator translator;

    PacketType(Translator translator) {
        this.translator = translator;
    }

    public NetworkPacket makePacket(PacketInputStream pis) throws IOException {
        return translator.translate(pis);
    }

    public static PacketType getType(int index){
        return PacketType.values()[index];
    }

    interface Translator {
        NetworkPacket translate(PacketInputStream pis) throws IOException;
    }
}
