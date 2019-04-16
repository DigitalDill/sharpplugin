package us.howtosharp.sharpplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.utilities.Utils;

import java.sql.SQLException;
import java.sql.Statement;

public class DeletePunishCommand implements CommandExecutor {
    SharpPlugin plugin;
    public DeletePunishCommand(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 2) {
            sender.sendMessage(Utils.tc("&cProper usage is /delpunish <bans|mutes> <id>"));
            return true;
        }
        String type = args[0];
        String id = args[1];
        try {
            Statement statement = Utils.newStatement(plugin);
            if(statement.execute(String.format("DELETE FROM %s WHERE id=%s", type, id))) {
                sender.sendMessage(Utils.tc("&cThat punishment could not be removed!"));
                return true;
            } else {
                sender.sendMessage(Utils.tc("&aThat punishment has been removed!"));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
