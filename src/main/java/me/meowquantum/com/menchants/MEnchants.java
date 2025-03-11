package me.meowquantum.com.menchants;

import me.meowquantum.com.menchants.Commands.MenuCommand;
import me.meowquantum.com.menchants.Events.MenuListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MEnchants extends JavaPlugin {

    private static MEnchants instance;
    private static Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault not found or no economy plugin detected! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        getCommand("encantamientos").setExecutor(new MenuCommand());

        saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &aActivado correctamente."));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cDesactivado correctamente."));
    }

    public static MEnchants getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
