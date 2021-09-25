package cz.larkyy.ldungeons.chamber;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import cz.larkyy.ldungeons.BossOrb;
import cz.larkyy.ldungeons.LDungeons;
import cz.larkyy.ldungeons.arena.Arena;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SpawnerChamber {

    private final Location location;
    private BossOrb bossOrb;
    private ArmorStand as;
    private final CMIHologram hologram;
    private final Arena arena;

    public SpawnerChamber(Location location, Arena arena) {
        this.arena = arena;
        this.location = location;
        hologram = new CMIHologram("SpawnerChamber-"+locationString(), location);
        CMI.getInstance().getHologramManager().addHologram(hologram);

        spawn();
    }

    private void spawnHolo() {
        hologram.setLoc(location);
        hologram.refresh();
        hologram.setLines(Arrays.asList(
                "&6Spawner Chamber",
                "",
                "&fPut in a &eBoss Orb&f!",
                "&7Right-Click with an Orb"
        ));

        if (!hologram.isEnabled()) {
            hologram.enable();
        }

        hologram.update();
    }

    public void updateHolo(int seconds, int needed) {
        hologram.setLines(Arrays.asList(
                "&6Spawner Chamber",
                "",
                "&fSpawning &e%boss% &fin".replace("%boss%", bossOrb.getBoss()),
                "%seconds% seconds!".replace("%seconds%",(needed-seconds)+""),
                "%progressbar%".replace("%progressbar%",generateProgressBar(seconds,needed))
        ));

        hologram.update();
    }

    private String generateProgressBar(int seconds, int needed) {
        int progress = (int)((double)seconds/needed*20);

        StringBuilder bar = new StringBuilder();
        for (int i = 1; i <= 20; i++) {
            if (progress >= i) {
                bar.append("&a|");
            } else {
                bar.append("&7|");
            }
        }

        return bar.toString();
    }

    public void spawn() {
        as = (ArmorStand) location.getWorld().spawnEntity(getAsLocation(), EntityType.ARMOR_STAND);
        as.getEquipment().setItem(EquipmentSlot.HEAD,new ItemStack(Material.PLAYER_HEAD));
        as.setInvisible(true);
        as.setPersistent(false);
        as.setGravity(false);

        spawnHolo();
    }

    public void despawn() {
        if (as != null) {
            as.remove();
            as = null;
        }
        if (hologram.isEnabled()) {
            hologram.disable();
        }
    }

    public void activateSpawning(BossOrb bossOrb) {

        this.bossOrb = bossOrb;
        as.setMarker(true);
        new SpawningChamber(this).runTaskTimer(LDungeons.getMain(),0,1);

        hologram.setLoc(location.clone().add(0,1,0));
        hologram.refresh();
    }

    public void spawnBoss() {
        despawn();
        try {
            arena.setBoss(MythicMobs.inst().getAPIHelper().spawnMythicMob(bossOrb.getBoss(), location));
        } catch (InvalidMobTypeException e) {
            spawn();
        }
    }

    public String locationString() {
        return location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ();
    }

    public void setAs(ArmorStand as) {
        this.as = as;
    }

    public Location getLocation() {
        return location;
    }
    public Location getAsLocation() {
        return location.clone().add(0,-3.3,0);
    }

    public ArmorStand getAs() {
        return as;
    }

    public Arena getArena() {
        return arena;
    }

    public BossOrb getBossOrb() {
        return bossOrb;
    }
}
