package cz.larkyy.ldungeons;

import cz.larkyy.ldungeons.arena.Arena;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaUpdateRunnable extends BukkitRunnable {

    @Override
    public void run() {

        for (Arena a : getManager().getArenas().values()) {

            if (a.getBoss() != null) {
                if (a.getBoss().isDead()) {
                    a.getSpawnerChamber().spawn();
                    a.setBoss(null);
                } else if (!a.getBoss().isDead() && a.getSpawnerChamber().getAs() != null) {
                    a.getSpawnerChamber().despawn();
                }
            }
        }
    }

    private Manager getManager() {
        return LDungeons.getMain().getManager();
    }
}
