package us.howtosharp.sharpplugin.utilities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.howtosharp.sharpplugin.SharpPlugin;

import java.awt.*;
import java.sql.*;
import java.util.UUID;

public class Punishment {
    public Player target;
    public String author;
    public String reason;
    public String type;
    private SharpPlugin plugin;
    private JDA bot;
    private int id = 0;

    public Punishment(Player target, String author, String reason, SharpPlugin plugin, String type) {
        this.target = target;
        this.author = author;
        this.reason = reason;
        this.plugin = plugin;
        bot = plugin.discordBot.bot;
        this.type = type.toUpperCase();
    }
    public Punishment(Player target, String author, String reason, SharpPlugin plugin, String type, int id) {
        this.target = target;
        this.author = author;
        this.reason = reason;
        this.plugin = plugin;
        bot = plugin.discordBot.bot;
        this.type = type.toUpperCase();
        this.id = id;
    }

    public void discord() {
        MessageChannel punishmentChannel = bot.getTextChannelById("533737100944736256");
        EmbedBuilder eb = new EmbedBuilder();
        String ids = id == 0 ? "KICK" : Integer.toString(id);

        eb.setTitle("Punishment #" + ids);
        eb.setColor(Color.red);

        eb.addField("Executor", author, false);
        eb.addField("Target", target.getName(), false);
        eb.addField("Punishment Type", type, false);
        eb.addField("Reason", reason, false);

        punishmentChannel.sendMessage(eb.build()).queue();
    }

    public static int banPlayer(Player p, String reason, String author, SharpPlugin plugin) {
        return banPlayer(p.getUniqueId(), reason, author, plugin);
    }

    public static int banPlayer(String name, String reason, String author, SharpPlugin plugin) {
        return banPlayer(Bukkit.getPlayer(name).getUniqueId(), reason, author, plugin);
    }
    public static int banPlayer(UUID uuid, String reason, String author, SharpPlugin plugin) {
         String host, database, username, password;
         int port;
         Connection connection;
        host = plugin.getConfig().getString("mysql.host");
        port = plugin.getConfig().getInt("mysql.port");
        database = plugin.getConfig().getString("mysql.database");
        username = plugin.getConfig().getString("mysql.username");
        password = plugin.getConfig().getString("mysql.password");
        try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO bans (uuid,reason,author) VALUES ('%s','%s','%s')", uuid.toString(), reason, author));
            ResultSet rs = statement.executeQuery(String.format("SELECT id FROM bans WHERE uuid='%s';", uuid.toString(), reason, author));
            if(rs.next()) {
                return rs.getInt("id");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;


    }
    public static int mutePlayer(Player p, String reason, String author, SharpPlugin plugin) {
        return mutePlayer(p.getUniqueId(), reason, author, plugin);
    }

    public static int mutePlayer(String name, String reason, String author, SharpPlugin plugin) {
        return mutePlayer(Bukkit.getPlayer(name).getUniqueId(), reason, author, plugin);
    }
    public static int mutePlayer(UUID uuid, String reason, String author, SharpPlugin plugin) {
        String host, database, username, password;
        int port;
        Connection connection;
        host = plugin.getConfig().getString("mysql.host");
        port = plugin.getConfig().getInt("mysql.port");
        database = plugin.getConfig().getString("mysql.database");
        username = plugin.getConfig().getString("mysql.username");
        password = plugin.getConfig().getString("mysql.password");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO mutes (uuid,reason,author) VALUES ('%s','%s','%s')", uuid.toString(), reason, author));
            ResultSet rs = statement.executeQuery(String.format("SELECT id FROM mutes WHERE uuid='%s';", uuid.toString(), reason, author));
            if(rs.next()) {
                return rs.getInt("id");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;


    }
}
