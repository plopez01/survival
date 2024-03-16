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
import me.plopez.survivalgame.rendering.Renderable;
import me.plopez.survivalgame.rendering.Renderer;
import me.plopez.survivalgame.rendering.Terrain;
import me.plopez.survivalgame.util.RangeConstrain;

import processing.core.PApplet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.plopez.survivalgame.Globals.sketch;


public class GameClient extends Client {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.CYAN + "CLIENT> " + Logger.RESET);
    Terrain terrain;
    public Camera camera;
    Renderer renderer;
    List<WorldObject> worldObjects;
    Map<String, Player> players = new HashMap<>();
    Player myPlayer = new Player("Pau" + sketch.random(10000), 10000, sketch.color(sketch.random(255), sketch.random(255), sketch.random(255)));


    public GameClient(PApplet parent, String address, int port) throws IOException {
        super(parent, address, port);

        terrain = new Terrain(parent, 4, 5000, 0.5f, 5, 5);
        camera = new Camera(parent, 16, 20, new RangeConstrain(10, 80), 1, 4);
        renderer = new Renderer(parent, camera, 0.01f);

        // Download world
        try {
            // Server handshake
            PacketInputStream is = new PacketInputStream(input);

            ServerHandshake sHandshake = (ServerHandshake) PacketType.getType(is.readByte()).makePacket(is);
            ((Survival) parent).seedManager.setSeed(sHandshake.seed);
            worldObjects = sHandshake.worldObjects;
            log.debug("Handled server handshake.");

            for (WorldObject worldObject : worldObjects) {
                if (worldObject instanceof Renderable r) renderer.add(r);
                if (worldObject instanceof Player p) players.put(p.getName(), p);
            }

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

        terrain.renderAt(camera);
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
                    Player player = players.get(moveCommand.entityID);
                    player.commandMove(moveCommand.target);
                }
                case CLIENT_DISCONNECT -> {
                    ClientDisconnect clientDisconnect = (ClientDisconnect) inPacket;

                    Player localPlayer = players.get(clientDisconnect.player.getName());
                    removePlayer(localPlayer);
                }
                default -> throw new IllegalStateException("Unexpected value: " + inPacket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerPlayer(Player player) throws DuplicatePlayerException {
        if (players.containsKey(player.getName())) throw new DuplicatePlayerException();
        log.debug("Registering player " + player.getName());
        worldObjects.add(player);
        players.put(player.getName(), player);
        renderer.add(player);
    }

    public void removePlayer(Player player){
        renderer.remove(player);
        worldObjects.remove(player);
        players.remove(player.getName());
        log.debug("Removing player " + player.getName());
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public Camera getCamera() {
        return camera;
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
}
