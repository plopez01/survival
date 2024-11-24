package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.objects.entity.Player;
import me.plopez.survivalgame.server.GameServer;

public class ClientConnect extends BroadcastPacket {
    public Player player;

    public ClientConnect(Player player) {
        this.player = player;
    }

    @Override
    public void handleClient(GameClient client) {
        try {
            client.registerPlayer(player);
        } catch (DuplicatePlayerException e) {
            client.log.warn("Client tried to register an already existing player.");
        }
    }

    @Override
    public void handleServer(GameServer server, Client client) {
        try {
            server.registerPlayer(player);
        } catch (DuplicatePlayerException e) {
            server.log.warn("Client (" + client.ip() + ") tried to connect with name "
                    + player.getName() +
                    " but another player with the same name already exists.");
            server.disconnect(client);
        }
    }
}
