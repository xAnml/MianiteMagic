package net.anmlmc.mianitemagic.Mana;

import net.anmlmc.mianitemagic.Main;
import net.anmlmc.mianitemagic.Spell.Spell;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kishanpatel on 12/12/15.
 */

public class ManaManager {

    public int getMana(Player player) {

        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT Mana FROM SpellPlayerInfo WHERE UUID='" + player.getUniqueId() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("Mana");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setMana(player, 0);
        return 0;
    }

    public boolean canUseSpell(Player player, Spell spell) {
        if (getMana(player) == -1) {
            return false;
        } else {
            if (getMana(player) - spell.getCost(spell) >= 0) {
                return true;
            }
        }
        return false;
    }

    public void useSpell(Player player, Spell spell) {
        setMana(player, getMana(player) - spell.getCost(spell));
        player.sendMessage("&a-" + spell.getCost(spell) + " mana (Using the " + spell.getName() + " spell)");

    }

    public void setMana(Player player, int balance) {
        try {
            Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `Mana`) VALUES ('" + player.getUniqueId() + "','" + balance + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
