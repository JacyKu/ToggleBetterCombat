package dev.togglebettercombat;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Server-side toggle state, stored in the player's NBT.
 */
public final class ToggleService {
    private static final String LEGACY_TAG = "togglebettercombat_disabled";

    private ToggleService() {
    }

    public static boolean isDisabled(ServerPlayerEntity player) {
        return ((TogglePlayerNbt) player).togglebettercombat$isDisabled();
    }

    public static void toggle(ServerPlayerEntity player) {
        boolean disabled = !isDisabled(player);
        setDisabled(player, disabled);
        ToggleNotifier.send(player, disabled, true);
    }

    public static void setDisabled(ServerPlayerEntity player, boolean disabled) {
        ((TogglePlayerNbt) player).togglebettercombat$setDisabled(disabled);
    }

    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (isDisabled(oldPlayer)) {
            setDisabled(newPlayer, true);
        }
    }

    public static void sync(ServerPlayerEntity player) {
        if (player.getCommandTags().remove(LEGACY_TAG)) {
            setDisabled(player, true);
        }
        ToggleNotifier.send(player, isDisabled(player), false);
    }
}
