package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.objects.entity.Entity;
import me.plopez.survivalgame.server.GameServer;
import processing.core.PVector;

import java.util.UUID;

public class MoveCommand extends BroadcastPacket {
    public UUID entityID;
    public PVector target;
    public MoveCommand(UUID entityID, PVector target) {
        this.entityID = entityID;
        this.target = target;
    }

    @Override
    public void handleClient(GameClient client) {
        Entity entity = client.getWorld().getEntity(entityID);
        entity.commandMove(target);
    }

    @Override
    public void handleServer(GameServer server, Client client) {
        Entity entity = server.getWorld().getEntity(entityID);

        entity.transform.set(new PVector(-target.x, -target.y, entity.transform.z));
    }
}
