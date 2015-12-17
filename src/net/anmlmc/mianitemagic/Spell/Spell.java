package net.anmlmc.mianitemagic.Spell;

import net.anmlmc.mianitemagic.Utils.MessageManager;

/**
 * Created by kishanpatel on 12/12/15.
 */
public enum Spell {

    CURE("Cure", SpellCategory.GENERAL, 0, false, "Removes all of your active negative potion effects."),
    DIVING("Diving", SpellCategory.GENERAL, 1, false, "Gives you water breathing for a certain time."),
    LEAP("Leap", SpellCategory.GENERAL, 0, false, "Gives you jump boost for a certain time."),
    SHADOW("Shadow", SpellCategory.GENERAL, 0, true, "Teleports you to the player you are looking at."),

    EXPLODE("Explode", SpellCategory.OFFENSIVE, 1, true, "Creates an explosion at the player you are looking at."),
    SHOCK("Shock", SpellCategory.OFFENSIVE, 2, true, "Strikes the player you are looking at with lightening."),
    FIREBALL("Fireball", SpellCategory.OFFENSIVE, 3, true, "Spawns a fireball at the player you are looking at."),
    FREEZE("Freeze", SpellCategory.OFFENSIVE, 4, true, "Freezes the player you are looking at for a certain time."),
    SLOWNESS("Slowness", SpellCategory.OFFENSIVE, 0, true, "Gives slowness to the player you are looking at for a certain time."),

    DASH("Dash", SpellCategory.DEFENSIVE, 1, false, "Gives you speed for a certain time."),
    INVISIBILITY("Invisibility", SpellCategory.DEFENSIVE, 0, false, "Gives you invisibility for a certain time.");

    public SpellManager sm = new SpellManager();
    public MessageManager mm = new MessageManager();
    String name;
    SpellCategory category;
    int level;
    boolean needsTarget;
    String description;

    Spell(String name, SpellCategory category, int level, boolean needsTarget, String description) {
        this.name = name;
        this.category = category;
        this.level = level;
        this.needsTarget = needsTarget;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCost(Spell spell) {
        return sm.getCost(spell);

    }

    public int getUsageExperience(Spell spell) {
        return sm.getUsageExperience(spell);
    }

    public String getName() {
        return name;
    }

    public SpellCategory getCategory() {
        return category;
    }

    public int getLevel() {
        return level;
    }

    public boolean needsTarget() {
        return needsTarget;
    }
}
