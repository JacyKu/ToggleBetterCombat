package dev.togglebettercombat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Resolves the toggle state on either side. Servers read the persistent tag;
 * clients use the value synced by the server.
 */
public final class ToggleState {
    private ToggleState() {
    }

    public static boolean isDisabled(PlayerEntity player) {
        if (player.getWorld() != null && player.getWorld().isClient()) {
            return ToggleClientState.isDisabled();
        }
        return player instanceof ServerPlayerEntity serverPlayer
                && ToggleService.isDisabled(serverPlayer);
    }
}
