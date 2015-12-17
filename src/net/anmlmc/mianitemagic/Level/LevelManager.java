package net.anmlmc.mianitemagic.Level;

import net.anmlmc.mianitemagic.Main;
import net.anmlmc.mianitemagic.Spell.Spell;
import net.anmlmc.mianitemagic.Spell.SpellCategory;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kishanpatel on 12/12/15.
 */
public class LevelManager {

    public int getLevel(Player player, SpellCategory spellCategory) {

        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT " + spellCategory.getName() + "Level FROM SpellPlayerInfo WHERE UUID='" + player.getUniqueId() + "'");
            if (resultSet.next()) {
                return resultSet.getInt(spellCategory.getName() + "Level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLevel(player, spellCategory, 0);
        return 0;
    }

    public int getExperience(Player player, SpellCategory spellCategory) {
        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT " + spellCategory.getName() + "Experience FROM SpellPlayerInfo WHERE UUID='" + player.getUniqueId() + "'");
            if (resultSet.next()) {
                return resultSet.getInt(spellCategory.getName() + "Experience");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setExperience(player, spellCategory, 0);
        return 0;
    }

    public void setExperience(Player player, SpellCategory spellCategory, int experience) {
        try {
            Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `" + spellCategory.getName() + "Experience`) VALUES ('" + experience + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void useSpell(Player player, Spell spell) {
        setExperience(player, spell.getCategory(), getExperience(player, spell.getCategory()) + spell.getUsageExperience(spell));
        player.sendMessage("&9+" + spell.getUsageExperience(spell) + " " + spell.getCategory().getName().toLowerCase() + " experience (Using the " + spell.getName() + " spell)");
    }

    public void setLevel(Player player, SpellCategory spellCategory, int level) {
        try {
            Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `" + spellCategory.getName() + "Level`) VALUES ('" + level + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean canLevelUp(Player player, SpellCategory spellCategory) {
        int level = getLevel(player, spellCategory);
        int experience = getExperience(player, spellCategory);
        if (experience >= ((level * 3) + 50))
            return true;
        return false;
    }

    public void levelUp(Player player, SpellCategory spellCategory) {

        if (canLevelUp(player, spellCategory)) {
            try {
                Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `" + spellCategory.getName() + "Level`) VALUES ('" + player.getUniqueId() + "','" + (getLevel(player, spellCategory) + 1) + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean canUseSpell(Player player, Spell spell) {
        if (getLevel(player, spell.getCategory()) >= spell.getLevel()) {
            return true;
        }
        return false;
    }


}
