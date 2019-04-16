package us.howtosharp.sharpplugin.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.howtosharp.sharpplugin.SharpPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Utils {
    public static String tc(String m) {
        return ChatColor.translateAlternateColorCodes('&', m);
    }
    public static String prefix(String m, Object... args) {return tc(String.format("&bSharpPlugin&e >>&r " + m, args)); }
    public static Statement newStatement(SharpPlugin plugin) throws SQLException { return plugin.connection.createStatement(); }
    public static boolean isBanned(Player p, SharpPlugin plugin) {
        try {
            Statement statement = newStatement(plugin);
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM bans WHERE uuid='%s'", p.getUniqueId().toString()));
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public static String getString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }
    public static ItemStack getItem(String s) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }
    public static String arrayToString(Object[] theAray, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < theAray.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            String item = theAray[i].toString();
            sb.append(item);
        }
        return sb.toString();
    }
}
