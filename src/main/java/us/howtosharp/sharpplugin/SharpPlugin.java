package us.howtosharp.sharpplugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import us.howtosharp.sharpplugin.commands.*;
import us.howtosharp.sharpplugin.utilities.DiscordBot;
import us.howtosharp.sharpplugin.utilities.EventListener;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class SharpPlugin extends JavaPlugin {

    public DiscordBot discordBot = new DiscordBot(this);
    private String host, database, username, password;
    private int port;
    public Connection connection;
    public HashMap<UUID, Boolean> allowedPlayers = new HashMap<>();

    private YamlConfiguration playersConfig = new YamlConfiguration();
    private File playersFile = new File(getDataFolder(), "players.yml");

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;


    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        discordBot.prepare();
        this.getCommand("punish").setExecutor(new PunishCommand(this));
        this.getCommand("delpunish").setExecutor(new DeletePunishCommand(this));
        this.getCommand("addresource").setExecutor(new AddResourceSpawner(this));
        this.getCommand("delresource").setExecutor(new DeleteResourceSpawner(this));


        host = getConfig().getString("mysql.host");
        port = getConfig().getInt("mysql.port");
        database = getConfig().getString("mysql.database");
        username = getConfig().getString("mysql.username");
        password = getConfig().getString("mysql.password");

        try {
            openConnection();
            Statement statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

    public void verifyPlayer(OfflinePlayer player) {
        verifyPlayer(player.getUniqueId());
    }

    public void verifyPlayer(UUID uuid) {
        allowedPlayers.put(uuid, true);
    }

    public YamlConfiguration getPlayersConfig() {
        return this.playersConfig;
    }

    public void savePlayersConfig() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }
}
