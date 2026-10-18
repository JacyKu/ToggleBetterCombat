package dev.togglebettercombat;

/**
 * Implemented on {@code PlayerEntity} by {@code PlayerNbtMixin}.
 */
public interface TogglePlayerNbt {
    String NBT_KEY = "togglebettercombat_disabled";
    String NBT_MIGRATED_KEY = "togglebettercombat_migrated";

    boolean togglebettercombat$isDisabled();

    void togglebettercombat$setDisabled(boolean disabled);

    boolean togglebettercombat$isMigrated();

    void togglebettercombat$setMigrated(boolean migrated);
}
