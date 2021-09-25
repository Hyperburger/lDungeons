package cz.larkyy.ldungeons;

import cz.larkyy.ldungeons.arena.Arena;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Manager {

    private final Map<String, Arena> arenas;
    private final Map<Player, Arena> editingPlayers;

    public Manager() {
        this.arenas = new HashMap<>();
        this.editingPlayers = new HashMap<>();
    }

    public boolean addArena(CommandSender sender, Arena arena) {
        if (arena.getSpawn() == null) {
            sender.sendMessage("§cSpawn is null!");
            return false;
        }

        if (arena.getMiddle() == null) {
            sender.sendMessage("§cMiddle is null!");
            return false;
        }

        if (arena.getMaxLoc() == null) {
            sender.sendMessage("§cFirst position is null!");
            return false;
        }

        if (arena.getMinLoc() == null) {
            sender.sendMessage("§cSecond position is null!");
            return false;
        }

        if (arena.getTeleportLoc() == null) {
            sender.sendMessage("§cTeleport location is null!");
            return false;
        }

        this.arenas.put(arena.getId(),arena);

        if (arena.isSetup()) {
            arena.activate();
        }

        return true;
    }

    public boolean isBossInArena(Arena a) {
        boolean isSpawnedBoss = false;

        for (ActiveMob aMob : MythicMobs.inst().getMobManager().getActiveMobs()) {
            Location loc = abstractLocToLoc(aMob.getLocation());

            if (a.getMiddle().getWorld().getName().equals(loc.getWorld().getName())) {
                if (loc.toVector().isInAABB(a.getMinLoc().toVector(),a.getMaxLoc().toVector())) {
                    isSpawnedBoss = true;
                    break;
                }
            }
        }
        return isSpawnedBoss;
    }

    public Location abstractLocToLoc(AbstractLocation aLoc) {
        return new Location(Bukkit.getWorld(aLoc.getWorld().getName()), aLoc.getX(),aLoc.getY(),aLoc.getZ());
    }

    public Arena getArena(String id) {
        return arenas.get(id);
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public void addEditingPlayer(Player p, Arena a) {
        editingPlayers.put(p,a);
    }

    public void removeEditingPlayer(Player p) {
        editingPlayers.remove(p);
    }

    public boolean isEditingPlayer(Player p) {
        return editingPlayers.containsKey(p);
    }

    public Arena getEditingArena(Player p) {
        return editingPlayers.get(p);
    }

    public boolean isArena(String id) {
        return arenas.containsKey(id);
    }
}
