package net.anmlmc.mianitemagic;

import net.anmlmc.mianitemagic.MySQL.MySQL;
import net.anmlmc.mianitemagic.Spell.Spell;
import net.anmlmc.mianitemagic.Spell.SpellManager;
import net.anmlmc.mianitemagic.Utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kishanpatel on 12/12/15.
 */

public class Main extends JavaPlugin {

    public static Main main;

    private static MySQL mySQL;
    private static SpellManager spellManager;
    private static MessageManager messageManager;

    public static Map<Player, Spell> getPlayerSpells() {
        return playerSpells;
    }

    private static Map<Player, Spell> playerSpells = new HashMap<>();

    public static SpellManager getSpellManager() {
        return spellManager;
    }
    public static MessageManager getMessageManager() {
        return messageManager;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static Main getMain() {
        return main;
    }

    private static void registerEvents(Plugin plugin, Listener listener) {

        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }


    @Override
    public void onEnable() {
        main = this;

       // mySQL = new MySQL();
       // mySQL.createTables();

        spellManager = new SpellManager();
        messageManager = new MessageManager();

        // mySQL.setConfig();
        spellManager.setConfig();


     //  getCommand("report").setExecutor(new ReportCommand());

     //   registerEvents(this, new ChatListener());

        Bukkit.getLogger().info("[MianiteMagic] Plugin has been enabled without any errors.");

    }

    @Override
    public void onDisable() {

        main = null;

    }
}
