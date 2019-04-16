package us.howtosharp.sharpplugin.utilities;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.gamemode.ResourceSpawner;
import us.howtosharp.sharpplugin.utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class EventListener implements Listener {
    private Map<Character, ItemStack> emptyRecipe = new HashMap<Character, ItemStack>();
    private SharpPlugin plugin;
    public EventListener(SharpPlugin plugin) {
        emptyRecipe.put('1', new ItemStack(Material.AIR));
        emptyRecipe.put('2', new ItemStack(Material.AIR));
        emptyRecipe.put('3', new ItemStack(Material.AIR));
        emptyRecipe.put('4', new ItemStack(Material.AIR));
        emptyRecipe.put('5', new ItemStack(Material.AIR));
        emptyRecipe.put('6', new ItemStack(Material.AIR));
        emptyRecipe.put('7', new ItemStack(Material.AIR));
        emptyRecipe.put('8', new ItemStack(Material.AIR));
        emptyRecipe.put('9', new ItemStack(Material.AIR));
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Connection conn = plugin.connection;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM bans WHERE uuid='%s';", uuid.toString()));
            while(rs.next()) {
                String reason = rs.getString("reason");
                e.getPlayer().kickPlayer(Utils.tc(reason + "\n&aAppeal at howtosharp.us/appeal"));
                e.setJoinMessage("");
            }
        } catch (SQLException ex) {
            plugin.discordBot.errorChannel.sendMessage(ex.getMessage());
            ex.printStackTrace();
        }

    }
    @EventHandler
    public void onAsyncChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Connection conn = plugin.connection;
        UUID uuid = p.getUniqueId();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM mutes WHERE uuid='%s';", uuid.toString()));
            while(rs.next()) {
                String reason = rs.getString("reason");
                e.setCancelled(true);
                p.sendMessage(Utils.tc("&cYou have been muted for the reason: " + reason));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            plugin.discordBot.errorChannel.sendMessage(ex.getMessage());

        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(Utils.isBanned(e.getPlayer(), plugin)) {
            e.setQuitMessage("");
        }
    }
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if(Utils.isBanned(e.getPlayer(), plugin)) {
            e.setLeaveMessage("");
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        try {
            Statement statement = Utils.newStatement(plugin);
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM blockspawners WHERE x=%s AND y=%s AND z=%s;", (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
            if(rs.next()) {
                long time = rs.getLong("time");
                Material mat = Material.getMaterial(rs.getString("material"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new ResourceSpawner(e.getBlock(), mat), time*20L);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ConfigurationSection worth = plugin.getConfig().getConfigurationSection("worth");
        try {
            double cost = worth.getDouble(e.getBlock().getType().toString().toLowerCase());
            Economy economy = plugin.getEconomy();
            economy.depositPlayer(e.getPlayer(), cost);
            if(cost > 20) {
                e.getPlayer().sendMessage(Utils.prefix("You have been paid %s for mining %s", Double.toString(cost), e.getBlock().getType().toString()));
            }
        } catch(NullPointerException ex) {
            ex.addSuppressed(ex);
        }
    }
}
