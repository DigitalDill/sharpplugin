package us.howtosharp.sharpplugin.utilities;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import us.howtosharp.sharpplugin.SharpPlugin;
import us.howtosharp.sharpplugin.discord.DiscordCommand;
import us.howtosharp.sharpplugin.discord.GetPunishment;

import javax.security.auth.login.LoginException;

public class DiscordBot extends ListenerAdapter {
    public TextChannel errorChannel;
    public JDA bot;
    private SharpPlugin plugin;
    public DiscordBot(SharpPlugin plugin) {
        this.plugin = plugin;
    }
    public void prepare() {
        try {
            DiscordBot db = new DiscordBot(plugin);
            bot = new JDABuilder(plugin.getConfig().getString("discord.token")).build();
            db.errorChannel = bot.getTextChannelById("557382750915657738");
            bot.addEventListener(db);
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getChannelType() == ChannelType.PRIVATE || event.getAuthor().isBot()) {
            return;
        }
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        Message message = event.getMessage();
        String text = message.getContentRaw();
        if(text.startsWith("sw!")) {
            String command = text.replace("sw!", "").split(" ")[0];
            String[] args = text.replace("sw!", "").replace(command, "").split(" ");
            switch(command) {
                default:
                    new DiscordCommand(plugin).run(channel, args, author);
                    break;
                case "punishment":
                    new GetPunishment(plugin).run(channel, args, author);
                    break;
            }
        }
    }
}
