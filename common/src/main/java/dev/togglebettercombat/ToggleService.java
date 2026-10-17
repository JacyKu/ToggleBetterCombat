package dev.togglebettercombat;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Server-side toggle state, persisted as a vanilla command tag.
 */
public final class ToggleService {
    public static final String TAG = "togglebettercombat_disabled";

    private ToggleService() {
    }

    public static boolean isDisabled(ServerPlayerEntity player) {
        return player.getCommandTags().contains(TAG);
    }

    public static void toggle(ServerPlayerEntity player) {
        boolean disabled = !isDisabled(player);
        setDisabled(player, disabled);
        ToggleNotifier.send(player, disabled, true);
    }

    public static void setDisabled(ServerPlayerEntity player, boolean disabled) {
        if (disabled) {
            player.addCommandTag(TAG);
        } else {
            player.getCommandTags().remove(TAG);
        }
    }

    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (isDisabled(oldPlayer)) {
            setDisabled(newPlayer, true);
        }
    }

    public static void sync(ServerPlayerEntity player) {
        ToggleNotifier.send(player, isDisabled(player), false);
    }
}
