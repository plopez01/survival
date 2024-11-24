package me.plopez.survivalgame.network.packet;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.objects.entity.Entity;
import me.plopez.survivalgame.objects.entity.Player;
import me.plopez.survivalgame.server.GameServer;

public class ClientDisconnect extends ForwardPacket {
    public Player player;

    public ClientDisconnect(Player player) {
        this.player = player;
    }

    @Override
    public void handleClient(GameClient client) {
        Player localPlayer = client.getWorld().getPlayer(player.getName());
        client.removePlayer(localPlayer);
    }

    @Override
    public void handleServer(GameServer server, Client client) {
        Player localPlayer = server.getWorld().getPlayer(player.getName());

        server.removePlayer(localPlayer);
        server.disconnect(client);
    }
}
