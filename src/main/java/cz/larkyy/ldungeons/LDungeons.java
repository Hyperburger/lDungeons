package cz.larkyy.ldungeons;

import cz.larkyy.commandslib.CommandBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class LDungeons extends JavaPlugin {

    private static LDungeons main;
    private Manager manager;

    @Override
    public void onEnable() {
        main = this;
        manager = new Manager();
        setupCmds();

        getServer().getPluginManager().registerEvents(new Listeners(),this);
        new ArenaUpdateRunnable().runTaskTimer(this,30,10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupCmds() {
        new CommandBuilder("ldungeons","§cOnly for Players!", "§cYou have no permission to do that!")
                .setPermission("ldungeons.admin")
                .addAlias("ldung","ldungeon")
                .setCanSendConsole(false)
                .build();
    }

    public static LDungeons getMain() {
        return main;
    }

    public Manager getManager() {
        return manager;
    }
}
