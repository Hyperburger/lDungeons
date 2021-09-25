package cz.larkyy.ldungeons.chamber;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawningChamber extends BukkitRunnable {

    private final SpawnerChamber spawnerChamber;
    private int tick = 0;
    private final float rpt = 1.4f/20;
    private final int neededSecs = 60;

    public SpawningChamber(SpawnerChamber spawnerChamber) {
        this.spawnerChamber =spawnerChamber;
    }

    @Override
    public void run() {

        Location newLoc = getAs().getLocation();
        newLoc.setYaw(getAs().getLocation().getYaw()+2);

        newLoc.setY(Math.sin(tick*rpt)*0.6+getAsLocation().getY());

        getAs().teleport(newLoc);

        if (tick % 20 == 0 && tick != neededSecs*20) {
            spawnerChamber.updateHolo(tick/20,neededSecs);
        }

        if (tick == neededSecs*20) {
            spawnerChamber.spawnBoss();
            cancel();
        }
        tick++;
    }

    private ArmorStand getAs() {
        return spawnerChamber.getAs();
    }

    private Location getAsLocation() {
        return spawnerChamber.getAsLocation();
    }
}
