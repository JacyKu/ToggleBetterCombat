package dev.togglebettercombat.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.network.TogglePayload;
import dev.togglebettercombat.network.ToggleStatePayload;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Client-only. Only referenced from the Fabric client entrypoint and the
 * NeoForge {@code Dist.CLIENT} mod class, so it is never loaded on a server.
 */
public final class ToggleBetterCombatClient {
    private static final KeyBinding TOGGLE_KEY = new KeyBinding(
            "key.togglebettercombat.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.togglebettercombat"
    );

    private static final KeyBinding CONFIG_KEY = new KeyBinding(
            "key.togglebettercombat.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.togglebettercombat"
    );

    private ToggleBetterCombatClient() {
    }

    public static void init() {
        ToggleClientConfig.load();
        KeyMappingRegistry.register(TOGGLE_KEY);
        KeyMappingRegistry.register(CONFIG_KEY);

        // S2C feedback packet from the server; the client picks action bar or toast.
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                ToggleStatePayload.ID,
                ToggleStatePayload.CODEC,
                (payload, context) -> context.queue(() -> ToggleClientNotifier.show(payload.enabled()))
        );

        ClientTickEvent.CLIENT_POST.register(client -> {
            // Always consume clicks, even while a screen is open, so they do not
            // queue up and fire when the player closes the screen.
            while (TOGGLE_KEY.wasPressed()) {
                if (client.player != null
                        && client.currentScreen == null
                        && NetworkManager.canServerReceive(TogglePayload.ID)) {
                    NetworkManager.sendToServer(TogglePayload.INSTANCE);
                }
            }

            while (CONFIG_KEY.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(new ToggleConfigScreen(null));
                }
            }
        });

        ToggleBetterCombat.LOGGER.info("Toggle Better Combat client loaded (key: {})", TOGGLE_KEY.getBoundKeyTranslationKey());
    }
}
