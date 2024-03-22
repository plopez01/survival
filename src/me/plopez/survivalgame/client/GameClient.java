package me.plopez.survivalgame.client;

import me.plopez.survivalgame.Survival;
import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.packet.*;
import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.objects.WorldObject;
import me.plopez.survivalgame.rendering.*;
import me.plopez.survivalgame.util.RangeConstrain;

import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.plopez.survivalgame.Globals.sketch;


public class GameClient extends Client {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.CYAN + "CLIENT> " + Logger.RESET);
    public Camera camera;
    Player myPlayer;

    World world;
    public GameClient(String address, int port, String playerName) throws IOException {
        super(sketch, address, port);

        myPlayer = new Player(playerName, 10000, sketch.color(sketch.random(255), sketch.random(255), sketch.random(255)));
        camera = new Camera(16, 20, new RangeConstrain(10, 80), 1, 4);

        world = new World(
                new Terrain(4, 5000, 0.5f, 5, 5),
                new CameraRenderer(0.01f, camera));

        // Download world
        try {
            // Server handshake
            PacketInputStream is = new PacketInputStream(input);

            ServerHandshake sHandshake = (ServerHandshake) PacketType.getType(is.readByte()).makePacket(is);
            ((Survival) sketch).seedManager.setSeed(sHandshake.seed);
            world.setWorldObjects(sHandshake.worldObjects);
            log.debug("Handled server handshake.");
            log.info("World seed: " + sHandshake.seed);

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

        world.render();
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
                    Player player = world.getPlayer(moveCommand.entityID);
                    player.commandMove(moveCommand.target);
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

    public void onClick(PVector pos) {
        MoveCommand cmd = new MoveCommand(getMyPlayer().getName(), getCamera().getRelativeWorldMouse());
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
