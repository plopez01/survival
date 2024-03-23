package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.World;

import java.util.List;
import java.io.*;

public class WorldData extends NetworkPacket {
    public World world;

    public WorldData(World world) {
        super(PacketType.WORLD_DATA);
        this.world = world;
    }

    public WorldData(PacketInputStream ois) throws IOException {
        super(PacketType.WORLD_DATA);
        //noinspection unchecked
        world = (World) ois.readPackedObject();
    }

    /** ----------------------------------------------------------------------
     * | Seed | Number of objects (int) | WorldObject 1 | WorldObject 2 ... |
     * ----------------------------------------------------------------------
     */
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(world);
    }
}
