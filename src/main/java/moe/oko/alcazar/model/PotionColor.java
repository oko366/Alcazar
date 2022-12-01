package moe.oko.alcazar.model;

import org.bukkit.potion.PotionType;

public enum PotionColor {
    INSTANT_HEAL(16196650),
    STRENGTH(9643050),
    SPEED(8236491),
    REGEN(13458352),
    FIRE_RESISTANCE(14915649),
    DEFAULT(3355443);
    private int color;

    PotionColor(int color) { this.color = color; }

    public static int getColor(PotionType type) {
        switch (type) {
            case INSTANT_HEAL -> { return INSTANT_HEAL.color; }
            case STRENGTH -> { return STRENGTH.color; }
            case SPEED -> { return SPEED.color; }
            case REGEN -> { return REGEN.color; }
            case FIRE_RESISTANCE -> { return FIRE_RESISTANCE.color; }
            default -> { return DEFAULT.color; }
        }
    }
}
