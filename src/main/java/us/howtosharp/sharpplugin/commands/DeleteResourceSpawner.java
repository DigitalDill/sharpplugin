package us.howtosharp.sharpplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.utilities.Utils;

import java.sql.SQLException;
import java.sql.Statement;

public class DeleteResourceSpawner implements CommandExecutor {
    SharpPlugin plugin;
    public DeleteResourceSpawner(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 3) {
            sender.sendMessage(Utils.tc("&eProper usage is /addresource <x> <y> <z>"));
            return true;
        }

        try {
            Statement statement = Utils.newStatement(plugin);
            if(!statement.execute(String.format("DELETE FROM blockspawners WHERE x=%s AND y=%s AND z=%s;", args[0], args[1], args[2]))) {
                sender.sendMessage(Utils.tc("&aYou have removed that location as a resource spawner!"));
                return true;
            } else {
                sender.sendMessage(Utils.tc("&cThat block could not be removed as a resource spawner."));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
