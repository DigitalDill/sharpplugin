package us.howtosharp.sharpplugin.gamemode;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ResourceSpawner implements Runnable {
    Block block;
    Material mat;
    public ResourceSpawner(Block block, Material mat) {
        this.block = block;
        this.mat = mat;
    }

    @Override
    public void run() {
        block.setType(mat);
    }
}
