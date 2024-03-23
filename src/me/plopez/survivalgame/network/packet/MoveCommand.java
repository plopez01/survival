package me.plopez.survivalgame.network.packet;

import processing.core.PVector;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class MoveCommand extends NetworkPacket {
    public UUID entityID;
    public PVector target;
    public MoveCommand(UUID entityID, PVector target) {
        super(PacketType.MOVE_COMMAND);
        this.entityID = entityID;
        this.target = target;
    }

    public MoveCommand(PacketInputStream pis) throws IOException {
        super(PacketType.MOVE_COMMAND);
        entityID = (UUID) pis.readPackedObject();
        target = (PVector) pis.readPackedObject();
    }

    @Override
    protected void writeTo(ObjectOutputStream stream) throws IOException {
        stream.writeObject(entityID);
        stream.writeObject(target);
    }
}
