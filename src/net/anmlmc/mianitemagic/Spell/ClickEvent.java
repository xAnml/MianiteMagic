package net.anmlmc.mianitemagic.Spell;

import net.anmlmc.mianitemagic.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by kishanpatel on 12/13/15.
 */
public class ClickEvent implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = e.getItem();
        SpellManager sm = Main.getSpellManager();

        if(!heldItem.equals(sm.getWand(sm.getActiveSpell(player)))) {

        }
    }
}
