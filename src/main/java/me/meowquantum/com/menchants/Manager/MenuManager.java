package me.meowquantum.com.menchants.Manager;

import me.meowquantum.com.menchants.MEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    public static void openEnchantMenu(Player player) {
        String title = MEnchants.getInstance().getConfig().getString("menu.title", "&8&lEncantamientos").replace("&", "§");
        Inventory menu = Bukkit.createInventory(null, 27, title);

        ConfigurationSection enchants = MEnchants.getInstance().getConfig().getConfigurationSection("menu.enchants");
        if (enchants != null) {
            int slot = 0;
            for (String enchantKey : enchants.getKeys(false)) {
                if (slot >= menu.getSize()) break;

                String enchantmentKey = enchants.getString(enchantKey + ".key");
                Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                if (enchantment == null) continue;

                String name = enchants.getString(enchantKey + ".name", "&a" + enchantKey).replace("&", "§");
                List<String> lore = enchants.getStringList(enchantKey + ".lore");
                List<String> formattedLore = new ArrayList<>();
                for (String line : lore) {
                    formattedLore.add(line.replace("&", "§"));
                }

                ItemStack book = new ItemStack(Material.BOOK);
                ItemMeta meta = book.getItemMeta();
                meta.setDisplayName(name);
                meta.setLore(formattedLore);
                book.setItemMeta(meta);

                menu.setItem(slot++, book);
            }
        }

        player.openInventory(menu);
    }

    public static String getKeyByName(String enchantName) {
        ConfigurationSection enchants = MEnchants.getInstance().getConfig().getConfigurationSection("menu.enchants");
        if (enchants == null) {
            return null;
        }

        for (String key : enchants.getKeys(false)) {
            String name = enchants.getString(key + ".name", "").replace("&", "§");
            if (name.equals(enchantName)) {
                return enchants.getString(key + ".key");
            }
        }
        return null;
    }

    public static int getPrice(String enchantKey) {
        ConfigurationSection enchants = MEnchants.getInstance().getConfig().getConfigurationSection("menu.enchants");
        if (enchants == null) {
            return 0;
        }
        for (String key : enchants.getKeys(false)) {
            if (enchantKey.equalsIgnoreCase(enchants.getString(key + ".key"))) {
                int price = enchants.getInt(key + ".price", 0);
                return price;
            }
        }
        return 0;
    }

    public static double getMultiplier(String enchantKey) {
        ConfigurationSection enchants = MEnchants.getInstance().getConfig().getConfigurationSection("menu.enchants");
        if (enchants == null) {
            return 1.0;
        }
        for (String key : enchants.getKeys(false)) {
            if (enchantKey.equalsIgnoreCase(enchants.getString(key + ".key"))) {
                double multiplier = enchants.getDouble(key + ".multiplier", 1.0);
                return multiplier;
            }
        }
        return 1.0;
    }

    public static int getMaxLevel(String enchantKey) {
        ConfigurationSection enchants = MEnchants.getInstance().getConfig().getConfigurationSection("menu.enchants");
        if (enchants == null) {
            return 1;
        }
        for (String key : enchants.getKeys(false)) {
            if (enchantKey.equalsIgnoreCase(enchants.getString(key + ".key"))) {
                int maxLevel = enchants.getInt(key + ".max", 1);
                return maxLevel;
            }
        }
        return 1;
    }
}
