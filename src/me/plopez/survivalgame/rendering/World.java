package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.entities.Player;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.objects.WorldObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World implements Renderable {
    List<WorldObject> worldObjects;
    Map<String, Player> players = new HashMap<>();
    Terrain terrain;

    CameraRenderer renderer;

    public World(Terrain terrain, CameraRenderer renderer){
        this.terrain = terrain;
        this.renderer = renderer;
    }

    public void setWorldObjects(List<WorldObject> worldObjects) {
        this.worldObjects = worldObjects;

        for (WorldObject worldObject : worldObjects) {
            if (worldObject instanceof Renderable r) renderer.add(r);
            if (worldObject instanceof Player p) players.put(p.getName(), p);
        }
    }

    public List<WorldObject> getWorldObjects(){
        return worldObjects;
    }

    public Player getPlayer(String playerName){
        return players.get(playerName);
    }

    public void addPlayer(Player player) throws DuplicatePlayerException
    {
        if (players.containsKey(player.getName())) throw new DuplicatePlayerException();
        players.put(player.getName(), player);
        addObject(player);
    }

    public void removePlayer(Player player) {
        players.remove(player.getName());
        removeObject(player);
    }

    public void removePlayer(String playerName) {
        Player player = players.get(playerName);
        removePlayer(player);
    }

    public void addObject(WorldObject object){
        worldObjects.add(object);
        if (object instanceof Renderable r) renderer.add(r);
    }

    public void removeObject(WorldObject object){
        worldObjects.remove(object);
        if (object instanceof Renderable r) renderer.remove(r);
    }

    @Override
    public void render() {
        terrain.renderAt(renderer.getCam());
        renderer.render();
    }

    @Override
    public void renderText() {

    }
}
