package us.howtosharp.sharpplugin.utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

public class TestRunnable implements Runnable {
    PlayerDropItemEvent e;
    public TestRunnable(PlayerDropItemEvent e) {
        this.e = e;
    }
    @Override
    public void run() {
        for(int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                e.getPlayer().sendMessage(e.getItemDrop().getLocation().toString());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
