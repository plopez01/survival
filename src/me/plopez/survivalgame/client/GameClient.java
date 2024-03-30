package me.plopez.survivalgame.client;

import me.plopez.survivalgame.Survival;
import me.plopez.survivalgame.objects.entity.Entity;
import me.plopez.survivalgame.objects.entity.Player;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.packet.*;
import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.rendering.*;
import me.plopez.survivalgame.util.RangeConstrain;

import processing.core.PVector;

import java.io.IOException;

import static me.plopez.survivalgame.Globals.sketch;


public class GameClient extends Client {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.CYAN + "CLIENT> " + Logger.RESET);
    public Camera camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);
    CameraRenderer renderer = new CameraRenderer(1, camera);
    Player myPlayer;

    World world;
    public GameClient(String address, int port, String playerName) throws IOException {
        super(sketch, address, port);

        myPlayer = new Player(playerName, 100, sketch.color(sketch.random(255), sketch.random(255), sketch.random(255)));
        // Download world
        try {
            // Server handshake
            PacketInputStream is = new PacketInputStream(input);

            WorldData worldData = (WorldData) PacketType.getType(is.readByte()).makePacket(is);
            world = worldData.world;
            ((Survival) sketch).seedManager.setSeed(worldData.world.getSeed());

            log.debug("Handled server handshake.");
            log.info("World seed: " + world.getSeed());

            ClientConnect cHandshake = new ClientConnect(myPlayer);
            output.write(cHandshake.serialize());
            log.debug("Sending connect message to server.");
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public void tick() {
        handleIncomingPackets();

        world.getTerrain().renderAt(camera);
        renderer.render(world.getRenderables());
        renderer.render();
    }

    private void handleIncomingPackets(){
        try {
            if (input.available() <= 0) return;
            PacketInputStream is = new PacketInputStream(input);
            NetworkPacket inPacket = PacketType.getType(is.readByte()).makePacket(is);

            log.debug("Incoming packet " + inPacket.getType());

            switch (inPacket.getType()){
                case CLIENT_CONNECT -> {
                    ClientConnect clientConnect = (ClientConnect) inPacket;

                    try {
                        registerPlayer(clientConnect.player);
                    } catch (DuplicatePlayerException e) {
                        log.warn("Client tried to register an already existing player.");
                    }
                }
                case MOVE_COMMAND -> {
                    MoveCommand moveCommand = (MoveCommand) inPacket;
                    Entity entity = world.getEntity(moveCommand.entityID);
                    entity.commandMove(moveCommand.target);
                }
                case CLIENT_DISCONNECT -> {
                    ClientDisconnect clientDisconnect = (ClientDisconnect) inPacket;

                    Player localPlayer = world.getPlayer(clientDisconnect.player.getName());
                    removePlayer(localPlayer);
                }
                default -> throw new IllegalStateException("Unexpected value: " + inPacket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerPlayer(Player player) throws DuplicatePlayerException {
        log.debug("Registering player " + player.getName());
        world.addPlayer(player);
    }

    public void removePlayer(Player player){
        log.debug("Removing player " + player.getName());
        world.removePlayer(player.getName());
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public Camera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }

    public void onClick(PVector pos) {
        MoveCommand cmd = new MoveCommand(getMyPlayer().getId(), getCamera().getRelativeWorldMouse());
        try {
            output.write(cmd.serialize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDisconnect(){
        super.onDisconnect();
        log.info("Exiting...");

        ClientDisconnect disconnectPacket = new ClientDisconnect(myPlayer);

        try {
            output.write(disconnectPacket.serialize());
        } catch (IOException e) {
            log.warn("Failed to notify disconnection to server.");
        }
    }

    public static GameClient connect(String address, int port, String playerName) {
        GameClient client = null;
        System.out.print("Connecting to server...");
        while(client == null) {
            System.out.print('.');
            try {
                client = new GameClient(address, port, playerName);
            } catch (IOException e) {
                sketch.delay(1000);
            }
        }
        client.log.info("Connected to server!");
        return client;
    }
}
