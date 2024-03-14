package me.plopez.survivalgame.client;

import me.plopez.survivalgame.Survival;
import me.plopez.survivalgame.entities.Player;
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
import java.util.List;

import static me.plopez.survivalgame.Globals.sketch;


public class GameClient extends Client {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.CYAN + "CLIENT> " + Logger.RESET);
    Terrain terrain;
    public Camera camera;
    Renderer renderer;
    List<WorldObject> worldObjects;

    Player myPlayer = new Player("Pau", 10000, sketch.color(sketch.random(255), sketch.random(255), sketch.random(255)));


    public GameClient(PApplet parent, String address, int port) throws IOException {
        super(parent, address, port);

        terrain = new Terrain(parent, 4, 5000, 0.5f, 5, 5);
        camera = new Camera(parent, 16, 20, new RangeConstrain(10, 80), 1, 4);
        renderer = new Renderer(parent, camera, 0.01f);

        renderer.add(myPlayer);

        try {
            // Server handshake
            PacketInputStream is = new PacketInputStream(input);

            ServerHandshake sHandshake = (ServerHandshake) PacketType.getType(is.readByte()).makePacket(is);
            ((Survival) parent).seedManager.setSeed(sHandshake.seed);
            worldObjects = sHandshake.worldObjects;
            log.debug("Handled handshake");

            for (WorldObject worldObject : worldObjects) {
                if (worldObject instanceof Renderable r) renderer.add(r);
            }

            ClientConnect cHandshake = new ClientConnect(myPlayer);
            output.write(cHandshake.serialize());
            log.debug("Seding connect message to server.");
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

                    // If it is ourselves then ignore the message
                    if (!clientConnect.player.getName().equals(myPlayer.getName()))
                    {
                        worldObjects.add(clientConnect.player);
                        renderer.add(clientConnect.player);
                    } else log.debug("Ignoring selfconnect packet");

                }
                default -> throw new IllegalStateException("Unexpected value: " + inPacket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public Camera getCamera() {
        return camera;
    }
}
