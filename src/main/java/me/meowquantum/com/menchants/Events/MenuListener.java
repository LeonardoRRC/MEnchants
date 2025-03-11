package me.meowquantum.com.menchants.Events;

import me.meowquantum.com.menchants.MEnchants;
import me.meowquantum.com.menchants.Manager.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {

    private final Economy economy = MEnchants.getEconomy();

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        String menuTitle = MEnchants.getInstance().getConfig().getString("menu.title").replace("&", "§");
        if (!event.getView().getTitle().equalsIgnoreCase(menuTitle)) {
            return;
        }

        event.setCancelled(true);

        if (economy == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fEl sistema de economía no está configurado correctamente. Contacta a un administrador."));
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.getType().equals(Material.BOOK)) {
            return;
        }

        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fDebes tener un objeto en la mano para encantarlo."));
            return;
        }

        String enchantName = clickedItem.getItemMeta().getDisplayName();
        String enchantKey = MenuManager.getKeyByName(enchantName);

        if (enchantKey == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fEncantamiento inválido."));
            return;
        }

        Enchantment enchantment = Enchantment.getByName(enchantKey);
        if (enchantment == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fEncantamiento inválido."));
            return;
        }

        if (!isValidForItem(enchantment, itemInHand)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cEste encantamiento no se puede aplicar al objeto seleccionado."));
            return;
        }

        int basePrice = MenuManager.getPrice(enchantKey);
        double multiplier = MenuManager.getMultiplier(enchantKey);
        int maxLevel = MenuManager.getMaxLevel(enchantKey);

        ItemMeta meta = itemInHand.getItemMeta();
        int currentLevel = itemInHand.containsEnchantment(enchantment) ? itemInHand.getEnchantmentLevel(enchantment) : 0;

        if (currentLevel >= maxLevel) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cEste objeto ya tiene el nivel máximo de este encantamiento."));
            return;
        }

        int nextLevel = currentLevel + 1;
        int cost = (int) (basePrice * Math.pow(multiplier, currentLevel));

        if (economy.getBalance(player) < cost) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &cNesecitas &c&l" + cost + " &cMonedas para comprar este encantamiento."));
            return;
        }

        economy.withdrawPlayer(player, cost);

        meta.addEnchant(enchantment, nextLevel, true);
        itemInHand.setItemMeta(meta);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5Encantamientos&8] &8▶ &fEncantamiento aplicado correctamente. &aNivel: &a&l" + nextLevel));
        player.sendTitle(" ", "§c-§l" + cost + " §cMonedas");
    }

    private boolean isValidForItem(Enchantment enchantment, ItemStack item) {
        boolean valid = enchantment.canEnchantItem(item);
        return valid;
    }
}
