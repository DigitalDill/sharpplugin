package us.howtosharp.sharpplugin.discord;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import us.howtosharp.sharpplugin.SharpPlugin;

public class DiscordCommand {
    SharpPlugin plugin;
    public DiscordCommand(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    public void run(MessageChannel returnChannel, String[] args, User author) {
        String display = String.join(",", args);
        returnChannel.sendMessage("That command is unknown! Unknown arguments: " + display).queue();
    }
}
