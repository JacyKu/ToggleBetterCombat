package dev.togglebettercombat;

/**
 * Implemented on {@code PlayerEntity} by {@code PlayerNbtMixin}.
 */
public interface TogglePlayerNbt {
    String NBT_KEY = "togglebettercombat_disabled";

    boolean togglebettercombat$isDisabled();

    void togglebettercombat$setDisabled(boolean disabled);
}
