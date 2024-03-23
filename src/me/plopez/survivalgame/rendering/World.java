package me.plopez.survivalgame.rendering;

import me.plopez.survivalgame.objects.Tree;
import me.plopez.survivalgame.objects.entities.Entity;
import me.plopez.survivalgame.objects.entities.Player;
import me.plopez.survivalgame.exception.DuplicatePlayerException;
import me.plopez.survivalgame.objects.WorldObject;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    int seed;
    List<WorldObject> worldObjects = new ArrayList<>();
    Map<UUID, WorldObject> worldObjectsMap = new HashMap<>();
    List<Renderable> renderables = new ArrayList<>();
    Map<String, Player> players = new HashMap<>();
    Terrain terrain;

    public World(int seed, Terrain terrain){
        this.seed = seed;
        this.terrain = terrain;

        addObject(new Tree());
    }

    public World(int seed, Terrain terrain, List<WorldObject> worldObjects){
        this.seed = seed;
        this.terrain = terrain;
        this.worldObjects = worldObjects;
    }

    public List<Renderable> getRenderables(){
        return renderables;
    }

    public List<WorldObject> getWorldObjects(){
        return worldObjects;
    }

    public Player getPlayer(String playerName){
        return players.get(playerName);
    }

    public Entity getEntity(UUID id) throws IllegalArgumentException {
        if (worldObjectsMap.get(id) instanceof Entity entity) return entity;
        else throw new IllegalArgumentException("Object with id " + id.toString() + " is not an Entity");
    }

    public WorldObject getObject(UUID id) {
        return worldObjectsMap.get(id);
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
        worldObjectsMap.put(object.getId(), object);
        if (object instanceof Renderable r) renderables.add(r);
    }

    public void removeObject(WorldObject object){
        worldObjects.remove(object);
        worldObjectsMap.remove(object.getId());
        if (object instanceof Renderable r) renderables.remove(r);
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public int getSeed(){
        return seed;
    }
}
