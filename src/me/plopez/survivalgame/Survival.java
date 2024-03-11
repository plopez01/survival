package me.plopez.survivalgame;

import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.client.SeedManager;
import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.log.Debug;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.Server;
import me.plopez.survivalgame.network.packet.ServerHandshake;
import me.plopez.survivalgame.server.GameServer;
import processing.core.*;
import processing.event.MouseEvent;

import java.io.IOException;

public class Survival extends PApplet {

    Debug debug = new Debug(this);
    //TODO migrate to static class
    public SeedManager seedManager = new SeedManager(this);
    Player myPlayer = new Player(this, "Pau", 10000, color(random(255), random(255), random(255)));

    boolean host = true;
    GameServer server;
    GameClient client;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        System.out.println("Starting game.");
        //fullScreen();
        background(0);

        if (host) {
            server = new GameServer(this, 5000, seedManager.getSeed());
            server.log.info("Server started at port " + server.getPort());
        }
        System.out.println("Connecting to server...");
        client = new GameClient(this, "127.0.0.1", 5000);
        client.log.info("Connected to server!");

        client.log.info("World seed: " + seedManager.getSeed());
    }

    public void draw() {
        client.tick();

        debug.add("FPS", frameRate);
        if (host) {
            debug.add("Hosting server", host);
        }
        debug.add("Game seed", seedManager.getSeed());
        debug.add("Pos", client.camera.transform);
        debug.add("Zoom", client.camera.getZoom());
        debug.add("ScreenMouse", Mouse.getMouseDistFromCenter(this));
        debug.add("WorldMouse", client.camera.getRelativeWorldMouse());
        //debug.showPointer();
        debug.render();
    }

    PVector holdOrigin = new PVector();

    public void mouseWheel(MouseEvent event) {
        client.camera.updateZoom(-event.getCount());
    }

    public void mousePressed() {
        holdOrigin = new PVector(mouseX, mouseY);
    }

    public void mouseReleased() {
    }

    public void mouseClicked() {
        //player.commandMove(camera.getRelativeWorldMouse(mouse));
    }

    public void mouseDragged() {
        PVector mousePos = new PVector(mouseX, mouseY);

        PVector displacement = mousePos.sub(holdOrigin);
        client.camera.translate(client.camera.toWorldSpace(displacement));
        holdOrigin = new PVector(mouseX, mouseY);
    }

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Survival"}, new Survival());
    }
}