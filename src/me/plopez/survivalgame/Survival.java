package me.plopez.survivalgame;

import joptsimple.*;
import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.client.SeedManager;
import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.log.Debug;
import me.plopez.survivalgame.network.packet.MoveCommand;
import me.plopez.survivalgame.rendering.Renderer;
import me.plopez.survivalgame.server.GameServer;
import me.plopez.survivalgame.ui.UI;
import me.plopez.survivalgame.util.StartupOptions;
import processing.core.*;
import processing.event.MouseEvent;

import java.io.IOException;

public class Survival extends PApplet {

    Debug debug = new Debug(this);
    //TODO migrate to static class
    public SeedManager seedManager = new SeedManager(this);
    StartupOptions startupOptions;
    GameServer server;
    GameClient client;

    Renderer gameRenderer = new Renderer(1);

    Survival(StartupOptions startupOptions){
        this.startupOptions = startupOptions;
    }

    public void settings() {
        size(640, 480);
        //fullScreen();
    }

    public void setup() {
        Globals.sketch = this;
        if (startupOptions.xScreen() >= 0 && startupOptions.yScreen() >= 0)
            windowMove(startupOptions.xScreen(), startupOptions.yScreen());

        System.out.println("Starting game.");

        background(0);

        if (startupOptions.isHost()) {
            server = new GameServer(this, 5000, seedManager.getSeed());
            server.log.info("Server started at port " + server.getPort());
        }

        client = GameClient.connect("127.0.0.1", 5000, "Pau");
        gameRenderer.add(UI.connectUI());
    }

    public void draw() {
        if (server != null) server.tick();

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
        gameRenderer.render();
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
        PVector target = client.getCamera().getRelativeWorldMouse();
        //client.getMyPlayer().commandMove(target);
        MoveCommand cmd = new MoveCommand(client.getMyPlayer().getName(), target);
        try {
            client.output.write(cmd.serialize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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