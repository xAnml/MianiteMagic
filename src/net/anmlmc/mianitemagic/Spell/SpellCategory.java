package net.anmlmc.mianitemagic.Spell;

/**
 * Created by kishanpatel on 12/12/15.
 */
public enum SpellCategory {

    GENERAL("General"), DEFENSIVE("Defensive"), OFFENSIVE("Offensive");

    String name;

    SpellCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
