package dev.togglebettercombat;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.togglebettercombat.network.TogglePayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ToggleBetterCombat {
    public static final String MOD_ID = "togglebettercombat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private ToggleBetterCombat() {
    }

    public static void init() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                TogglePayload.ID,
                (buf, context) -> context.queue(() -> {
                    if (context.getPlayer() instanceof ServerPlayerEntity player) {
                        ToggleService.toggle(player);
                    }
                })
        );

        // Allows a joining client to request its state after its channels are ready.
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                TogglePayload.REQUEST_ID,
                (buf, context) -> context.queue(() -> {
                    if (context.getPlayer() instanceof ServerPlayerEntity player) {
                        ToggleService.sync(player);
                    }
                })
        );

        PlayerEvent.PLAYER_JOIN.register(ToggleService::applySavedState);
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) ->
                ToggleService.copyState(oldPlayer, newPlayer)
        );

        LOGGER.info("Toggle Better Combat loaded");
    }
}
