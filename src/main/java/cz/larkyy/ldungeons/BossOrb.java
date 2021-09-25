package cz.larkyy.ldungeons;

import org.bukkit.inventory.ItemStack;

public class BossOrb {

    private final int id;
    private final ItemStack is;
    private final String boss;

    public BossOrb(int id, ItemStack is, String boss) {
        this.id = id;
        this.is = is;
        this.boss = boss;
    }

    public int getId() {
        return id;
    }

    public ItemStack getIs() {
        return is;
    }

    public String getBoss() {
        return boss;
    }
}
