package me.meowquantum.com.menchants.Commands;

import me.meowquantum.com.menchants.MEnchants;
import me.meowquantum.com.menchants.Manager.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("menchants.reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cNo tienes permiso para usar este comando."));
                return true;
            }

            MEnchants.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fConfiguracion recargada con exito."));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cSolo los jugadores pueden usar este comando."));
            return true;
        }

        Player player = (Player) sender;
        MenuManager.openEnchantMenu(player);
        return true;
    }
}
