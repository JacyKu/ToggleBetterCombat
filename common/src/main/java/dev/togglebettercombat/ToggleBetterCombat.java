package dev.togglebettercombat;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.togglebettercombat.network.TogglePayload;
import dev.togglebettercombat.network.ToggleStatePayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common entrypoint. Runs on both the physical client and the dedicated server,
 * for both Fabric and NeoForge.
 */
public final class ToggleBetterCombat {
    public static final String MOD_ID = "togglebettercombat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private ToggleBetterCombat() {
    }

    public static void init() {
        // Client -> server: "please flip my toggle". The server owns the state.
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                TogglePayload.ID,
                TogglePayload.CODEC,
                (payload, context) -> context.queue(() -> {
                    if (context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
                        ToggleService.toggle(serverPlayer);
                    }
                })
        );

        // Command tags are part of the old player NBT. Apply them immediately on
        // join, then carry them over to the fresh entity created on respawn.
        PlayerEvent.PLAYER_JOIN.register(ToggleService::applySavedState);
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) ->
                ToggleService.copyState(oldPlayer, newPlayer)
        );

        // Dedicated servers need to advertise the S2C payload type. On a client,
        // the type is registered together with the receiver in client init.
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(ToggleStatePayload.ID, ToggleStatePayload.CODEC);
        }

        LOGGER.info("Toggle Better Combat loaded");
    }
}
