package net.anmlmc.mianitemagic.Spell;

import net.anmlmc.mianitemagic.Main;
import net.anmlmc.mianitemagic.Utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishanpatel on 12/12/15.
 */

public class SpellManager {

    MessageManager messageManager = Main.getMessageManager();

    public int getCost(Spell spell) {
        String path = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".Cost";
        if (Main.getMain().getConfig().contains(path))
            return Main.getMain().getConfig().getInt(path);
        return -1;
    }

    public int getUsageExperience(Spell spell) {
        String path = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".UsageExperience";
        if (Main.getMain().getConfig().contains(path))
            return Main.getMain().getConfig().getInt(path);
        return -1;
    }

    public void setConfig() {
        for (Spell spell : Spell.values()) {
            String cost = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".Cost";
            String experienceUsage = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".UsageExperience";

            if (!Main.getMain().getConfig().contains(cost)) {
                Main.getMain().getConfig().set(cost, 10);
            }
            if (!Main.getMain().getConfig().contains(experienceUsage)) {
                Main.getMain().getConfig().set(experienceUsage, 10);
            }
        }

        Main.getMain().saveConfig();
    }

    public Spell getActiveSpell(Player player) {
        try {
            ResultSet resultSet = Main.getMySQL().getResultSet("SELECT ActiveSpell FROM SpellPlayerInfo WHERE UUID='" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                String result = resultSet.getString("ActiveSpell");
                for (Spell spell : Spell.values()) {
                    if (spell.getName().equalsIgnoreCase(result)) {
                        return spell;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setActiveSpell(Player player, Spell spell) {
        try {
            Main.getMySQL().executeUpdate("INSERT INTO `SpellPlayerInfo`(`UUID`, `ActiveSpell`) VALUES ('" + player.getUniqueId().toString() + "','" + spell.getName() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Spell> getActiveSpells(Player player) {
        List<Spell> spells = new ArrayList<>();
        String path = "ActiveSpellManager." + player.getUniqueId() + ".ActiveSpells";
        List<String> spellNames = Main.getMain().getConfig().getStringList(path);
        if (Main.getMain().getConfig().contains(path)) {
            for (String spellName : spellNames) {
                for (Spell spell : Spell.values()) {
                    if (spell.getName().equalsIgnoreCase(spellName)) {
                        spells.add(spell);
                    }
                }
            }
        }
        setActiveSpells(player, spells);
        return spells;
    }

    public void setActiveSpells(Player player, List<Spell> spells) {
        String path = "ActiveSpellManager." + player.getUniqueId() + ".ActiveSpells";
        List<String> spellNames = new ArrayList<>();
        for (Spell spell : spells) spellNames.add(spell.getName());
        Main.getMain().getConfig().set(path, spellNames);
        Main.getMain().saveConfig();
    }

    public void performSpell(Player player, Spell spell) {

        player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));

        if (spell.equals(Spell.CURE)) {
            for (PotionEffect effects : player.getActivePotionEffects()) {
                for (NegativeEffects negativeEffect : NegativeEffects.values()) {
                    if (effects.getType().getName().equalsIgnoreCase(negativeEffect.name())) {
                        player.removePotionEffect(effects.getType());
                    }
                }
            }
        } else if (spell.equals(Spell.DIVING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 10, 1));
        } else if (spell.equals(Spell.LEAP)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 1));
        } else if (spell.equals(Spell.DASH)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1));
        } else if (spell.equals(Spell.INVISIBILITY)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1));
        } else {
        }
    }

    public void performSpell(Player player, Player target, Spell spell) {

        if (!spell.equals(Spell.FREEZE))
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));

        if (spell.equals(Spell.SHADOW)) {
            player.teleport(target);
        } else if (spell.equals(Spell.EXPLODE)) {
            target.getWorld().createExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 4.0F, false, false);
        } else if (spell.equals(Spell.SHOCK)) {
            target.getWorld().strikeLightning(target.getLocation());
        } else if (spell.equals(Spell.FIREBALL)) {
            Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(target.getWorld(), target.getLocation().getYaw(), target.getLocation().getPitch());
            Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
        } else if (spell.equals(Spell.FREEZE)) {
            float priorSpeed = target.getWalkSpeed();
            target.setWalkSpeed(0F);
            long duration = 200L; //10 seconds
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have froze &b" + target.getName() + " &afor " + duration / 20 + " seconds."));
            target.sendMessage(messageManager.colorize("&b" + player.getName() + " &ahas performed the &5" + spell.getName() + " &aspell on you."));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {
                @Override
                public void run() {
                    target.setWalkSpeed(priorSpeed);
                }
            }, duration);
        } else if (spell.equals(Spell.SLOWNESS)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
        } else {
        }
    }

    public void iterateSpell(Player player) {

        List<Spell> spells = getActiveSpells(player);
        if (!spells.contains(getActiveSpell(player))) {
            setActiveSpell(player, spells.get(0));
        } else {
            int index = spells.indexOf(getActiveSpell(player));
            if (index == spells.size() - 1) {
                setActiveSpell(player, spells.get(0));
            } else {
                setActiveSpell(player, spells.get(index));
            }
        }
    }

    public ItemStack getWand(Spell spell) {
        ItemStack stack = new ItemStack(Material.STICK, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(messageManager.colorize("&7Wand"));
        List<String> lore = new ArrayList<>();
        lore.add(messageManager.colorize("&aActive Spell: &f" + spell.getName()));
        lore.add(messageManager.colorize("&aCost: &f$" + spell.getCost(spell)));
        lore.add(messageManager.colorize("&aDescription: &f" + spell.getDescription()));
        stack.setItemMeta(meta);
        return stack;
    }

}
