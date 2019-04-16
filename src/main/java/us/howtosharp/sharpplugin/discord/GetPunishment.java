package us.howtosharp.sharpplugin.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import us.howtosharp.sharpplugin.SharpPlugin;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;




public class GetPunishment extends DiscordCommand {
    public GetPunishment(SharpPlugin plugin) {
        super(plugin);
    }
    @Override
    public void run(MessageChannel returnChannel, String[] args, User author) {
        List<String> argList = Arrays.asList(args);
        Connection conn = plugin.connection;
        if(args.length != 4) {
            returnChannel.sendMessage("Proper usage is sw!punishment <bans|mutes> <id|uuid> <punishment id/uuid>").queue();
            return;
        }
        switch (args[1]) {
            case "bans":
                break;
            case "mutes":
                break;
            default:
                returnChannel.sendMessage("Proper usage is sw!punishment <bans|mutes> <id|uuid> <punishment id/uuid>").queue();
                return;
        }
        try {
            Statement statement = conn.createStatement();
            ResultSet rs;
            switch(args[2]) {
                case "id":
                    rs = statement.executeQuery("SELECT * FROM " + args[1] + " WHERE id='" + args[3] + "';");
                    break;
                case "uuid":
                    rs = statement.executeQuery("SELECT * FROM " + args[1] + " WHERE uuid='" + args[3] + "';");
                    break;
                default:
                    returnChannel.sendMessage("Proper usage is sw!punishment <bans|mutes> <id|uuid> <punishment id/uuid>").queue();
                    return;

            }
            while(rs.next()) {
                discord(returnChannel, rs.getString("author"), UUID.fromString(rs.getString("uuid")), args[1].toUpperCase().replace("S", ""), rs.getString("reason"), rs.getInt("id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void discord(MessageChannel channel, String author, UUID target, String type, String reason, int id) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Punishment #" + id);
        eb.setColor(Color.red);

        eb.addField("Executor", author, false);
        eb.addField("UUID", target.toString(), false);
        eb.addField("Username", plugin.getServer().getOfflinePlayer(target).getName(), false);
        eb.addField("Punishment Type", type, false);
        eb.addField("Reason", reason, false);

        channel.sendMessage(eb.build()).queue();
    }
}
