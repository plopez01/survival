package me.plopez.survivalgame;

import joptsimple.*;
import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.client.SeedManager;
import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.log.Debug;
import me.plopez.survivalgame.network.Client;
import me.plopez.survivalgame.network.Server;
import me.plopez.survivalgame.network.packet.ServerHandshake;
import me.plopez.survivalgame.server.GameServer;
import me.plopez.survivalgame.util.StartupOptions;
import processing.core.*;
import processing.event.MouseEvent;

import java.io.IOException;

public class Survival extends PApplet {

    Debug debug = new Debug(this);
    //TODO migrate to static class
    public SeedManager seedManager = new SeedManager(this);
    Player myPlayer = new Player(this, "Pau", 10000, color(random(255), random(255), random(255)));

    StartupOptions startupOptions;
    GameServer server;
    GameClient client;

    Survival(StartupOptions startupOptions){
        this.startupOptions = startupOptions;
    }

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        if (startupOptions.xScreen() >= 0 && startupOptions.yScreen() >= 0)
            windowMove(startupOptions.xScreen(), startupOptions.yScreen());

        System.out.println("Starting game.");
        //fullScreen();
        background(0);

        if (startupOptions.isHost()) {
            server = new GameServer(this, 5000, seedManager.getSeed());
            server.log.info("Server started at port " + server.getPort());
        }

        System.out.print("Connecting to server...");
        while(client == null) {
            System.out.print('.');
            try {
                client = new GameClient(this, "127.0.0.1", 5000);
            } catch (IOException e) {
                delay(1000);
            }
        }
        client.log.debug(client);

        client.log.info("Connected to server!");

        client.log.info("World seed: " + seedManager.getSeed());
    }

    public void draw() {
        client.tick();

        debug.add("FPS", frameRate);
        if (startupOptions.isHost()) {
            debug.add("Players connected", server.clientCount);
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
        OptionParser parser = new OptionParser();
        var help = parser.accepts("help", "Displays this help menu and exits").forHelp();
        var isSelfHost = parser.accepts("server", "Launches a self-hosted client");
        var xWindow = parser.accepts("xwindow", "Sets the screen's window x position on start").withRequiredArg().ofType(int.class).defaultsTo(-1);
        var yWindow = parser.accepts("ywindow", "Sets the screen's window y position on start").withRequiredArg().ofType(int.class).defaultsTo(-1);

        OptionSet programOptions;
        try {
            programOptions = parser.parse(args);
        } catch (OptionException e) {
            System.out.println("Incorrect parameters: " + e.getMessage());
            return;
        }
        if (programOptions.has(help)) {
            try {
                parser.printHelpOn(System.out);
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        StartupOptions options = new StartupOptions(
                programOptions.has(isSelfHost),
                programOptions.valueOf(xWindow),
                programOptions.valueOf(yWindow)
        );

        PApplet.runSketch(new String[]{"Survival"}, new Survival(options));
    }
}