package cz.larkyy.ldungeons;

import cz.larkyy.commandslib.events.CustomCommandEvent;
import cz.larkyy.ldungeons.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Listeners implements Listener {

    private ItemStack wandIs = new ItemStack(Material.BLAZE_ROD);
    private ItemStack middleIs = new ItemStack(Material.EMERALD);
    private ItemStack arenaSpawnIs = new ItemStack(Material.ENDER_EYE);
    private ItemStack lobbySpawnIs = new ItemStack(Material.ENDER_PEARL);
    private ItemStack bossOrbIs = new ItemStack(Material.DIAMOND);

    public Listeners() {
        ItemMeta wandMeta = wandIs.getItemMeta();
        ItemMeta middleMeta = middleIs.getItemMeta();
        ItemMeta arenaSpawnMeta = arenaSpawnIs.getItemMeta();
        ItemMeta lobbySpawnMeta = lobbySpawnIs.getItemMeta();
        ItemMeta bossOrbMeta = bossOrbIs.getItemMeta();

        wandMeta.setDisplayName("§6Area Wand");
        middleMeta.setDisplayName("§6Middle Location");
        arenaSpawnMeta.setDisplayName("§6Arena Spawn");
        lobbySpawnMeta.setDisplayName("§6Lobby Spawn");
        bossOrbMeta.setDisplayName("§bBoss Orb");

        wandIs.setItemMeta(wandMeta);
        middleIs.setItemMeta(middleMeta);
        arenaSpawnIs.setItemMeta(arenaSpawnMeta);
        lobbySpawnIs.setItemMeta(lobbySpawnMeta);
        bossOrbIs.setItemMeta(bossOrbMeta);
    }

    @EventHandler
    public void onCmd(CustomCommandEvent e) {
        if (e.getCmd().getLabel().equalsIgnoreCase("ldungeons")) {
            Player p = (Player) e.getSender();

            String[] args = e.getArgs();

            if (args.length == 0) {
                sendHelpMsg(p);
                return;
            }

            switch (args[0]) {
                case "create":
                    if (args.length < 2) {
                        if (getManager().isEditingPlayer(p)) {
                            if (!getManager().addArena(p, getManager().getEditingArena(p))) {
                                return;
                            }

                            p.sendMessage("Successfully created! Arena has been activated!");
                            getManager().removeEditingPlayer(p);
                            // SUCCESSFULLY CREATED ARENA
                        } else {
                            // UNKNOWN ARGUMENT
                        }
                    } else {
                        if (getManager().isEditingPlayer(p)) {
                            p.sendMessage("§cAlready editing!");
                            return;
                        }

                        createArenaCmd(p, args);
                    }
                    return;
                case "teleport":
                    if (args.length < 2) {
                        // UNKNOWN ARGUMENT
                        return;
                    }

                    if (getManager().isArena(args[1])) {
                        Arena arena = getManager().getArena(args[1]);

                        if (args.length == 2) {
                            p.teleport(arena.getTeleportLoc());
                            p.sendMessage("teleported!");
                        } else {
                            Player target = Bukkit.getPlayer(args[2]);
                            if (target==null) {
                                p.sendMessage("Unknown player!");
                                // UNKNOWN PLAYER
                                return;
                            }

                            target.teleport(arena.getTeleportLoc());
                            target.sendMessage("teleported!");
                        }
                    } else {
                        // UNKNOWN ARENA
                    }
                    return;
                case "edit":
                    if (args.length <2) {
                        return;
                    }

                    if (getManager().isEditingPlayer(p)) {
                        p.sendMessage("§cAlready editing!");
                        return;
                    }

                    if (getManager().isArena(args[1])) {
                        Arena arena = getManager().getArena(args[1]);

                        getManager().addEditingPlayer(p,arena);
                    }

                case "remove":
                    // TODO
                    return;
                case "giveitems":
                    p.getInventory().addItem(wandIs,middleIs,arenaSpawnIs,lobbySpawnIs,bossOrbIs);
                    return;
                default:
                    sendHelpMsg(p);
            }
        }
    }

    private void createArenaCmd(Player p, String[] args) {
        // arg1 - name
        p.sendMessage("You are now creating an Arena! (Name: "+args[1]+")");
        Arena arena = new Arena(args[1]);
        getManager().addEditingPlayer(p, arena);
    }

    private void sendHelpMsg(CommandSender s) {
        //TODO
        s.sendMessage("Help Page...");
    }

    private Manager getManager() {
        return LDungeons.getMain().getManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!getManager().isEditingPlayer(p)) {
            return;
        }

        Arena arena = getManager().getEditingArena(p);

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK ||
                e.getAction() == Action.LEFT_CLICK_BLOCK ||
                e.getAction() == Action.RIGHT_CLICK_AIR ||
                e.getAction() == Action.LEFT_CLICK_AIR
        ) {

            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                return;
            }

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (p.getInventory().getItemInMainHand().isSimilar(wandIs)) {
                    arena.setMinLoc(e.getClickedBlock().getLocation());
                    e.setCancelled(true);

                    p.sendMessage("Pos #1 loc set!");
                    return;
                }
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (p.getInventory().getItemInMainHand().isSimilar(wandIs)) {
                    arena.setMaxLoc(e.getClickedBlock().getLocation());
                    e.setCancelled(true);

                    p.sendMessage("Pos #2 loc set!");
                    return;
                }
            }

            // MIDDLE - middle of the arena
            if (p.getInventory().getItemInMainHand().isSimilar(middleIs)) {
                arena.setMiddle(p.getLocation().getBlock().getLocation());
                e.setCancelled(true);

                p.sendMessage("Middle loc set!");
                return;
            }

            // SPAWN - in front of the arena
            if (p.getInventory().getItemInMainHand().isSimilar(lobbySpawnIs)) {
                arena.setSpawn(p.getLocation());
                e.setCancelled(true);

                p.sendMessage("Spawn loc set!");
                return;
            }

            // TELEPORT LOC - teleport loc for the arena inside
            if (p.getInventory().getItemInMainHand().isSimilar(arenaSpawnIs)) {
                arena.setTeleportLoc(p.getLocation());
                e.setCancelled(true);

                p.sendMessage("Teleport loc set!");
                return;
            }
        }
    }

    @EventHandler
    public void onAsInteract(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();

        Arena a = clickedChamberAs(e.getRightClicked().getUniqueId());
        if (a != null) {
            e.setCancelled(true);

            if (p.getInventory().getItemInMainHand().isSimilar(bossOrbIs)) {
                a.getSpawnerChamber().activateSpawning(new BossOrb(1,bossOrbIs,"SkeletalKnight"));
            }
        }
    }

    private Arena clickedChamberAs(UUID id) {
        for (Arena a : getManager().getArenas().values()) {
            ArmorStand as = a.getSpawnerChamber().getAs();
            if (as == null) {
                continue;
            }
            if (as.getUniqueId() == id) {
                return a;
            }
        }
        return null;
    }
}
