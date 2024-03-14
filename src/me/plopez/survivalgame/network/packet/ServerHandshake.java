package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.network.packet.NetworkPacket;
import me.plopez.survivalgame.network.packet.PacketType;
import me.plopez.survivalgame.objects.WorldObject;
import java.util.List;
import java.io.*;

public class ServerHandshake extends NetworkPacket {
    public int seed;
    public List<WorldObject> worldObjects;

    public ServerHandshake(int seed, List<WorldObject> worldObjects) {
        super(PacketType.SERVER_HANDSHAKE);
        this.seed = seed;
        this.worldObjects = worldObjects;
    }

    public ServerHandshake(PacketInputStream ois) throws IOException {
        super(PacketType.SERVER_HANDSHAKE);
        seed = ois.readInt();
        //noinspection unchecked
        worldObjects = (List<WorldObject>) ois.readPacket();
    }

    /** ----------------------------------------------------------------------
     * | Seed | Number of objects (int) | WorldObject 1 | WorldObject 2 ... |
     * ----------------------------------------------------------------------
     */
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeInt(seed);

        stream.writeObject(worldObjects);
    }
}
