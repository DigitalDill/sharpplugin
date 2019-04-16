package us.howtosharp.sharpplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.utilities.Utils;

import java.sql.SQLException;
import java.sql.Statement;

public class AddResourceSpawner implements CommandExecutor {
    SharpPlugin plugin;
    public AddResourceSpawner(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 4) {
            sender.sendMessage(Utils.tc("&eProper usage is /addresource <x> <y> <z> <seconds>"));
            return true;
        }
        Location loc = new Location(((Player) sender).getWorld(),Integer.parseInt(args[0]),Integer.parseInt(args[1]), Integer.parseInt(args[2]));

        try {
            Statement statement = Utils.newStatement(plugin);
            if(!statement.execute(String.format("INSERT INTO blockspawners (x,y,z,time,material) VALUES ('%s','%s','%s','%s','%s');", args[0], args[1], args[2], args[3], loc.getBlock().getType().toString()))) {
                sender.sendMessage(Utils.tc("&aYou have added that location as a resource spawner!"));
                return true;
            } else {
                sender.sendMessage(Utils.tc("&cThat block could not be added as a resource spawner."));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
