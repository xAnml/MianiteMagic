package net.anmlmc.mianitemagic.Level;

import net.anmlmc.mianitemagic.Main;
import net.anmlmc.mianitemagic.Spell.SpellCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by kishanpatel on 12/12/15.
 */
public class LevelManager {

    public int getLevel(UUID uuid, SpellCategory spellCategory) {

        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT " + spellCategory.getName() + "Level FROM SpellPlayerInfo WHERE UUID='" + uuid.toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt(spellCategory.getName() + "Level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getExperience(UUID uuid, SpellCategory spellCategory) {

        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT " + spellCategory.getName() + "Experience FROM SpellPlayerInfo WHERE UUID='" + uuid.toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt(spellCategory.getName() + "Experience");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean canLevelUp(UUID uuid, SpellCategory spellCategory) {
        int level = getLevel(uuid, spellCategory);
        int experience = getExperience(uuid, spellCategory);
        if(experience >= ((level * 3) + 50))
            return true;
        return false;
    }

    public void levelUp(UUID uuid, SpellCategory spellCategory) {

        if (canLevelUp(uuid, spellCategory)) {
            try {
                Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `" + spellCategory.getName() + "Level`) VALUES ('" + uuid.toString() + "','" + (getLevel(uuid, spellCategory)+1) + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
