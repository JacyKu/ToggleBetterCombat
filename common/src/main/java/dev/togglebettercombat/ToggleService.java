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

    public static void applySavedState(ServerPlayerEntity player) {
        migrateLegacyTag(player);
    }

    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        TogglePlayerNbt oldState = (TogglePlayerNbt) oldPlayer;
        TogglePlayerNbt newState = (TogglePlayerNbt) newPlayer;
        newState.togglebettercombat$setDisabled(oldState.togglebettercombat$isDisabled());
        newState.togglebettercombat$setMigrated(oldState.togglebettercombat$isMigrated());
    }

    public static void sync(ServerPlayerEntity player) {
        migrateLegacyTag(player);
        ToggleNotifier.send(player, isDisabled(player), false);
    }

    private static void migrateLegacyTag(ServerPlayerEntity player) {
        TogglePlayerNbt state = (TogglePlayerNbt) player;
        if (state.togglebettercombat$isMigrated()) {
            return;
        }
        if (player.getCommandTags().remove(LEGACY_TAG)) {
            state.togglebettercombat$setDisabled(true);
        }
        state.togglebettercombat$setMigrated(true);
    }
}
