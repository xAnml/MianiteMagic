package net.anmlmc.mianitemagic.Events;

import net.anmlmc.mianitemagic.Level.LevelManager;
import net.anmlmc.mianitemagic.Main;
import net.anmlmc.mianitemagic.Mana.ManaManager;
import net.anmlmc.mianitemagic.Spell.Spell;
import net.anmlmc.mianitemagic.Spell.SpellManager;
import net.anmlmc.mianitemagic.Utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        Action action = e.getAction();

        MessageManager messageManager = Main.getMessageManager();
        SpellManager spellManager = Main.getSpellManager();
        LevelManager levelManager = Main.getLevelManager();
        ManaManager manaManager = Main.getManaManager();

        if (spellManager.getActiveSpell(player) == null) {
            player.sendMessage(messageManager.colorize("&cYou don't have any active spells."));
            return;
        }

        if (!heldItem.equals(spellManager.getWand(spellManager.getActiveSpell(player)))) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR) {
            Spell spell = null;
            for (Spell s : Spell.values()) {
                if (heldItem.equals(spellManager.getWand(s))) {
                    spell = s;
                }
            }

            if (spell == null) {
                return;
            }

            if (!spellManager.getActiveSpells(player).contains(spell)) {
                if (spellManager.getActiveSpells(player).size() == 0) {
                    player.getInventory().remove(heldItem);
                    player.sendMessage(messageManager.colorize("&cYou attempted to use a spell that is not part of your active spells."));
                } else {
                    spell = spellManager.getActiveSpells(player).get(0);
                    player.getInventory().setItemInHand(spellManager.getWand(spell));
                    player.sendMessage(messageManager.colorize("&aYour wand has been changed to execute a spell that is part of your active spell list."));
                }
            }

            if (!levelManager.canUseSpell(player, spell)) {
                player.sendMessage(messageManager.colorize("&cYou do not have a high enough level to use this spell"));
            }

            if (!manaManager.canUseSpell(player, spell)) {
                player.sendMessage(messageManager.colorize("&cYou do not have enough mana to use this spell."));
                return;
            }

            if (spellManager.getCost(spell) == -1 || spell.getUsageExperience(spell) == -1) {
                player.sendMessage(messageManager.colorize("&cThe configuration of this spell is incorrectly formatted."));
                return;
            }

            if (spell.needsTarget()) {
                Player target = messageManager.getTarget(player);

                if (target == null) {
                    player.sendMessage(messageManager.colorize("&cTarget not found."));
                    return;
                }
                spellManager.performSpell(player, target, spell);

            } else {
                spellManager.performSpell(player, spell);
            }

            manaManager.useSpell(player, spell);
            levelManager.useSpell(player, spell);
        } else if (action.equals(Action.LEFT_CLICK_AIR) && player.isSneaking()) {
            //TODO
        }


    }
}
