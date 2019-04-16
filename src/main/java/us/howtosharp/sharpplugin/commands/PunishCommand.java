package us.howtosharp.sharpplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtosharp.sharpplugin.utilities.Punishment;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.utilities.Utils;

import java.util.Calendar;

public class PunishCommand implements CommandExecutor {
    Calendar calender = Calendar.getInstance();

    Player punishTarget;
    Punishment punishment;
    SharpPlugin plugin;
    public PunishCommand(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(args.length != 3) {
                sender.sendMessage(Utils.tc("&eProper usage is /punish <player> <ban|kick|mute> <reason>"));
                return true;
            }
        String reason = args[2].replace("_", " ");
            if(Bukkit.getPlayer(args[0]) != null) {
                punishTarget = Bukkit.getPlayer(args[0]);
            } else {
                sender.sendMessage(Utils.tc("&cThat player could not be found."));
                return true;
            }
        punishment = new Punishment(punishTarget, sender.getName(), reason, plugin, args[1].toUpperCase());
            switch(args[1]) {
                case "ban":
                    int id = Punishment.banPlayer(punishTarget, reason, sender.getName(), plugin);
                    punishTarget.kickPlayer(Utils.tc(reason + "\n&aAppeal at howtosharp.us/appeal"));
                    punishment = new Punishment(punishTarget, sender.getName(), reason, plugin, args[1].toUpperCase(),id);
                    sender.sendMessage(Utils.tc("&aThat player has been banned! You can unban them with /delpunish bans " + id));


                    break;
                case "kick":
                    punishTarget.kickPlayer(Utils.tc(reason + "\n&aAppeal at howtosharp.us/appeal"));
                    break;
                case "mute":
                    int mid = Punishment.mutePlayer(punishTarget, reason, sender.getName(), plugin);
                    punishment = new Punishment(punishTarget, sender.getName(), reason, plugin, args[1].toUpperCase(), mid);
                    sender.sendMessage(Utils.tc("&aThat player has been muted! You can unmute them with /delpunish mutes " + mid));
                    break;
                default:
                    sender.sendMessage(Utils.tc("&eProper usage is /punish <player> <ban|kick|mute> <reason>"));
                    return true;
            }
            punishment.discord();
        return true;
    }
}
