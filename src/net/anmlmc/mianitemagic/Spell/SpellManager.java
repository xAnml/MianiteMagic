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
        if(Main.getMain().getConfig().contains(path))
            return Main.getMain().getConfig().getInt(path);
        return 0;
    }

    public int getUsageExperience(Spell spell) {
        String path = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".ExperienceUsage";
        if(Main.getMain().getConfig().contains(path))
            return Main.getMain().getConfig().getInt(path);
        return 0;
    }

    public void setConfig() {
        for(Spell spell : Spell.values()) {
            String cost = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".Cost";
            String experienceUsage = "SpellManager." + spell.getCategory().getName() + "." + spell.getName() + ".ExperienceUsage";

            if(!Main.getMain().getConfig().contains(cost)) {
                Main.getMain().getConfig().set(cost, 0);
            }
            if(!Main.getMain().getConfig().contains(experienceUsage)) {
                Main.getMain().getConfig().set(experienceUsage, 0);
            }
        }

        Main.getMain().saveConfig();
    }

    public boolean performSpell(Player player, Spell spell) {

        if(spell.equals(Spell.CURE)) {
            for(PotionEffect effects : player.getActivePotionEffects()){
                for(NegativeEffects negativeEffect : NegativeEffects.values()){
                    if(effects.getType().getName().equalsIgnoreCase(negativeEffect.name())){
                        player.removePotionEffect(effects.getType());
                    }
                }
            }
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));
            return true;
        }

        else if(spell.equals(Spell.DIVING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 10, 1));
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));
            return true;
        }

        else if(spell.equals(Spell.LEAP)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 1));
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));

            return true;
        }

        else if(spell.equals(Spell.DASH)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1));
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));

            return true;
        }

        else if(spell.equals(Spell.INVISIBILITY)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1));
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell."));

            return true;
        }
        else {
            return false;
        }
    }

    public boolean performSpell(Player player, Player target, Spell spell) {

        if(spell.equals(Spell.SHADOW)) {
            player.teleport(target);
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have been teleported to &b" + target.getName() + "&a."));

            return true;
        }

        else if(spell.equals(Spell.EXPLODE)) {
            target.getWorld().createExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 4.0F, false, false);
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have created an explosion at &b" + target.getName() + "&a's location."));

            return true;
        }

        else if(spell.equals(Spell.SHOCK)) {
            target.getWorld().strikeLightning(target.getLocation());
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have struck lightning at &b" + target.getName() + "&a's location."));
            return true;
        }

        else if(spell.equals(Spell.FIREBALL)) {
            Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(target.getWorld(), target.getLocation().getYaw(), target.getLocation().getPitch());
            Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have thrown a fireball at &b" + target.getName() + "&a."));
            return true;
        }

        else if(spell.equals(Spell.FREEZE)) {
            float priorSpeed = target.getWalkSpeed();
            target.setWalkSpeed(0F);
            long duration = 200L; //10 seconds
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell and have froze &b" + target.getName() + " &afor " + duration/20 + " seconds."));
            target.sendMessage(messageManager.colorize("&b" + player.getName() + " &ahas performed the &5" + spell.getName() + " &aspell on you."));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new Runnable() {
                @Override
                public void run() {
                    target.setWalkSpeed(priorSpeed);
                }
            }, duration);
            return true;
        }

        else if(spell.equals(Spell.SLOWNESS)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
            player.sendMessage(messageManager.colorize("&aYou have performed the &5" + spell.getName() + "&a spell on &b" + target.getName() + "&a."));
            return true;
        }

        else {
            return false;
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

    public Spell getActiveSpell(Player player) {
        return Main.getPlayerSpells().get(player);
    }

    public void setActiveSpell(Player player, Spell spell) {
        Main.getPlayerSpells().put(player, spell);
    }

    public List<Spell> getActiveSpells(Player player) {

        List<Spell> spells = new ArrayList<>();

        try {
            for(int x = 1; x <= 3; x++) {
                ResultSet resultSet = Main.getMySQL().getResultSet("SELECT Active" + x + " FROM SpellPlayerInfo WHERE UUID='" + player.getUniqueId().toString() + "'");
                if (resultSet.next()) {
                    for(Spell spell : Spell.values()) {
                        if(spell.getName().equalsIgnoreCase(resultSet.getString("Active" + x))) {
                            spells.add(spell);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spells;
    }

    public boolean iterateSpell(Player player) {
        List<Spell> spells = getActiveSpells(player);
        Spell spell = getActiveSpell(player);

        if(spells.size() == 0 || !(spells.contains(spell))) {
            return false;
        }
            if(spells.indexOf(spell) == (spells.size()-1)) {
                spell = spells.get(0);
            }
            else {
                spell = spells.get(spells.indexOf(spell)+1);
            }

            setActiveSpell(player, spell);
            return true;
    }





}
