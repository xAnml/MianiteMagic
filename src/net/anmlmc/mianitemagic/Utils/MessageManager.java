package net.anmlmc.mianitemagic.Utils;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishanpatel on 12/13/15.
 */
public class MessageManager {

    public String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public Player getTarget(Player p) {
        List<Entity> nearbyE = p.getNearbyEntities(50, 50, 50);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        BlockIterator bItr = new BlockIterator(p, 50);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((((bx - .75) <= ex) && (ex <= (bx + 1.75))) && (((bz - .75) <= ez) && (ez <= (bz + 1.75)))
                        && (((by - 1) <= ey) && (ey <= (by + 2.5)))) {
                    // entity is close enough, set target and stop
                    if (e instanceof Player) {
                        return (Player) e;
                    }
                }
            }
        }
        return null;

    }

}
