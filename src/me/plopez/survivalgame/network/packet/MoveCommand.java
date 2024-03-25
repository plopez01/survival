package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.vector.VectorF;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class MoveCommand extends NetworkPacket {
    public UUID entityID;
    public VectorF target;
    public MoveCommand(UUID entityID, VectorF target) {
        super(PacketType.MOVE_COMMAND);
        this.entityID = entityID;
        this.target = target;
    }

    public MoveCommand(PacketInputStream pis) throws IOException {
        super(PacketType.MOVE_COMMAND);
        entityID = (UUID) pis.readPackedObject();
        target = (VectorF) pis.readPackedObject();
    }

    @Override
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(entityID);
        stream.writeObject(target);
    }
}
