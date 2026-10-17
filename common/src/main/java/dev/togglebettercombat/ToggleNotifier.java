package dev.togglebettercombat;

import dev.architectury.networking.NetworkManager;
import dev.togglebettercombat.network.ToggleStatePayload;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Sends state to a client that has just used one of our C2S channels, so the
 * client necessarily has this mod installed.
 */
public final class ToggleNotifier {
    private ToggleNotifier() {
    }

    public static void send(ServerPlayerEntity player, boolean disabled, boolean notify) {
        NetworkManager.sendToPlayer(
                player,
                ToggleStatePayload.ID,
                ToggleStatePayload.write(disabled, notify)
        );
    }
}
