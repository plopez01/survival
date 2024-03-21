package me.plopez.survivalgame;

import joptsimple.*;
import me.plopez.survivalgame.client.GameClient;
import me.plopez.survivalgame.client.SeedManager;
import me.plopez.survivalgame.input.Mouse;
import me.plopez.survivalgame.log.Debug;
import me.plopez.survivalgame.network.packet.MoveCommand;
import me.plopez.survivalgame.server.GameServer;
import me.plopez.survivalgame.ui.ConnectUI;
import me.plopez.survivalgame.util.StartupOptions;
import processing.core.*;
import processing.event.MouseEvent;

import java.io.IOException;

import static me.plopez.survivalgame.Globals.focusedElement;
import static me.plopez.survivalgame.Globals.mainCanvas;

public class Survival extends PApplet {

    Debug debug = new Debug(this);
    //TODO migrate to static class
    public SeedManager seedManager = new SeedManager(this);
    StartupOptions startupOptions;
    GameServer server;
    GameClient client;

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

        mainCanvas = new ConnectUI((String ip, String playerName) -> {
            client = GameClient.connect(ip, 5000, playerName);
            return null;
        });
    }

    public void draw() {
        if (server != null) server.tick();

        debug.add("FPS", frameRate);

        if (client != null) {
            client.tick();

            if (startupOptions.isHost()) {
                debug.add("Players connected", server.clientCount);
            }

            debug.add("Game seed", seedManager.getSeed());
            debug.add("Pos", client.camera.transform);
            debug.add("Zoom", client.camera.getZoom());
            debug.add("ScreenMouse", Mouse.getMouseDistFromCenter(this));
            debug.add("WorldMouse", client.camera.getRelativeWorldMouse());
            //debug.showPointer();
        }

        debug.render();
        if (mainCanvas != null) mainCanvas.render();
    }

    PVector holdOrigin = new PVector();

    public void mouseWheel(MouseEvent event) {
        client.camera.updateZoom(-event.getCount());
    }

    public void mousePressed() {
        if (focusedElement != null) focusedElement.onMousePressed(Mouse.relativePos());
        holdOrigin = new PVector(mouseX, mouseY);
    }

    public void mouseReleased() {
        if (focusedElement != null) focusedElement.onMouseReleased(Mouse.relativePos());
    }

    public void mouseClicked() {
        if (mainCanvas != null) mainCanvas.onClick(Mouse.mousePos());

        /*PVector target = client.getCamera().getRelativeWorldMouse();
        MoveCommand cmd = new MoveCommand(client.getMyPlayer().getName(), target);
        try {
            client.output.write(cmd.serialize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void mouseDragged() {
        PVector mousePos = new PVector(mouseX, mouseY);
        PVector displacement = mousePos.sub(holdOrigin);

        if (focusedElement != null) focusedElement.onMouseDragged(displacement);

        client.camera.translate(client.camera.toWorldSpace(displacement));
        holdOrigin = new PVector(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        if (focusedElement != null) focusedElement.onKeyPressed(key);
    }

    public void keyReleased(){
        if (focusedElement != null) focusedElement.onKeyReleased(key);
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