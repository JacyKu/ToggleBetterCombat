package dev.togglebettercombat;

import dev.architectury.networking.NetworkManager;
import dev.togglebettercombat.network.ToggleStatePayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Sends the toggle feedback. Modded clients receive a state packet and render
 * it as configured (action bar or toast). Clients without this addon get the
 * action bar fallback so the toggle still gives feedback.
 */
public final class ToggleNotifier {
    private ToggleNotifier() {
    }

    public static void send(ServerPlayerEntity player, boolean enabled) {
        if (NetworkManager.canPlayerReceive(player, ToggleStatePayload.ID)) {
            NetworkManager.sendToPlayer(player, new ToggleStatePayload(enabled));
        } else {
            Text message = ToggleText.message(enabled);
            player.sendMessage(message, true);
        }
    }
}
