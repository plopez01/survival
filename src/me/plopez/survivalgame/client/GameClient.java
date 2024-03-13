package me.plopez.survivalgame.client;

import me.plopez.survivalgame.Survival;
import me.plopez.survivalgame.log.Logger;
import me.plopez.survivalgame.log.LoggingLevel;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.packet.PacketType;
import me.plopez.survivalgame.network.packet.ServerHandshake;
import me.plopez.survivalgame.objects.Camera;
import me.plopez.survivalgame.rendering.Renderer;
import me.plopez.survivalgame.rendering.Terrain;
import me.plopez.survivalgame.util.RangeConstrain;

import processing.core.PApplet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class GameClient extends Client {
    public Logger log = new Logger(LoggingLevel.ALL, Logger.CYAN + "CLIENT> " + Logger.RESET);
    Terrain terrain;
    public Camera camera;
    Renderer renderer;

    public GameClient(PApplet parent, String address, int port) throws IOException {
        super(parent, address, port);

        terrain = new Terrain(parent, 4, 5000, 0.5f, 5, 5);
        camera = new Camera(parent, 16, 20, new RangeConstrain(10, 80), 1, 4);
        renderer = new Renderer(parent, camera, 0.01f);
        try {
            ObjectInputStream is = new ObjectInputStream(input);
            
            ServerHandshake handshake = (ServerHandshake) PacketType.getType(is.readByte()).makePacket(is); //new ServerHandshake(is);
            ((Survival) parent).seedManager.setSeed(handshake.seed);
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public void tick() {
        terrain.renderAt(camera);
        renderer.render();
    }
}
