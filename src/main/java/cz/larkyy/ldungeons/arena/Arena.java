package cz.larkyy.ldungeons.arena;

import cz.larkyy.ldungeons.chamber.SpawnerChamber;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private Location teleportLoc;
    private Location middle;
    private Location spawn;
    private final String id;
    private final int slots;
    private final List<Player> joinedPlayers;
    private SpawnerChamber spawnerChamber;
    private Location minLoc;
    private Location maxLoc;
    private boolean setup;
    private Entity boss;

    public Arena(String id, Location minLoc, Location maxLoc, Location teleportLoc, Location middle, Location spawn) {
        setup = false;
        this.teleportLoc = teleportLoc;
        this.spawn = spawn;
        this.middle = middle;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.id = id;
        this.slots = 10;
        this.joinedPlayers = new ArrayList<>();
        setSpawnerChamber();
    }

    public Arena(String id) {
        setup = true;
        this.id = id;
        slots = 10;
        joinedPlayers = new ArrayList<>();
    }

    private void fixMinMaxLocations() {

        if (minLoc == null) {
            return;
        }

        if (maxLoc == null) {
            return;
        }

        Location oldLoc2 = minLoc.clone();

        if (maxLoc.getX() < oldLoc2.getX()) {
            minLoc.setX(maxLoc.getX());
            maxLoc.setX(oldLoc2.getX());
        }
        if (maxLoc.getY() < oldLoc2.getY()) {
            minLoc.setY(maxLoc.getY());
            maxLoc.setY(oldLoc2.getY());
        }
        if (maxLoc.getZ() < oldLoc2.getZ()) {
            minLoc.setZ(maxLoc.getZ());
            maxLoc.setZ(oldLoc2.getZ());
        }
    }

    public boolean isFree() {
        return (joinedPlayers.size() < slots);
    }

    public void joinArena(Player p) {
        addJoined(p);
        p.teleport(teleportLoc);
    }

    public void leaveArena(Player p) {
        p.teleport(spawn);
        removeJoined(p);
    }

    public void activate() {
        if (!setup) {
            return;
        }
        setup = false;

        setSpawnerChamber();
    }

    public String getId() {
        return id;
    }

    public int getSlots() {
        return slots;
    }

    public Location getMiddle() {
        return middle;
    }

    public void setMiddle(Location middle) {
        this.middle = middle;

        if (!setup) {
            spawnerChamber.despawn();
            setSpawnerChamber();
        }
    }

    public SpawnerChamber getSpawnerChamber() {
        return spawnerChamber;
    }

    private void setSpawnerChamber() {
        spawnerChamber = new SpawnerChamber(middle.clone().add(0.5,2.5,0.5),this);
    }

    public Location getMaxLoc() {
        return maxLoc;
    }

    public Location getMinLoc() {
        return minLoc;
    }

    public Location getTeleportLoc() {
        return teleportLoc;
    }

    public boolean isPlayerInAABB(Player p) {
        return (p.getLocation().toVector().isInAABB(minLoc.toVector(),maxLoc.toVector()));
    }

    public void addJoined(Player p) {
        joinedPlayers.add(p);
    }
    public void removeJoined(Player p) {
        joinedPlayers.remove(p);
    }

    public List<Player> getJoined() {
        return joinedPlayers;
    }

    public boolean isJoined(Player p) {
        return joinedPlayers.contains(p);
    }

    public Location getSpawn() {
        return spawn;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setMaxLoc(Location maxLoc) {
        this.maxLoc = maxLoc;
        fixMinMaxLocations();
    }

    public void setMinLoc(Location minLoc) {
        this.minLoc = minLoc;
        fixMinMaxLocations();
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void setTeleportLoc(Location teleportLoc) {
        this.teleportLoc = teleportLoc;
    }

    public Entity getBoss() {
        return boss;
    }

    public void setBoss(Entity boss) {
        this.boss = boss;
    }
}
