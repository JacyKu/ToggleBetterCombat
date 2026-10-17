package dev.togglebettercombat.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.ToggleClientState;
import dev.togglebettercombat.network.TogglePayload;
import dev.togglebettercombat.network.ToggleStatePayload;
import io.netty.buffer.Unpooled;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.bettercombat.client.BetterCombatClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

/**
 * Client-only. Loaded from the Fabric client entrypoint or Forge's
 * {@code DistExecutor} client branch.
 */
public final class ToggleBetterCombatClient {
    private static final KeyBinding TOGGLE_KEY = new KeyBinding(
            "key.togglebettercombat.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.togglebettercombat"
    );

    private static boolean synced;
    private static boolean suppressingBetterCombat;

    private ToggleBetterCombatClient() {
    }

    public static void init() {
        ToggleClientConfig.load();
        KeyMappingRegistry.register(TOGGLE_KEY);

        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                ToggleStatePayload.ID,
                (buf, context) -> {
                    boolean disabled = buf.readBoolean();
                    boolean notify = buf.readBoolean();
                    context.queue(() -> {
                        ToggleClientState.setDisabled(disabled);
                        if (notify) {
                            ToggleClientNotifier.show(!disabled);
                        }
                    });
                }
        );

        ClientTickEvent.CLIENT_POST.register(client -> {
            if (client.player == null) {
                synced = false;
                suppressingBetterCombat = false;
                ToggleClientState.setDisabled(false);
                return;
            }

            if (!synced && NetworkManager.canServerReceive(TogglePayload.REQUEST_ID)) {
                synced = true;
                NetworkManager.sendToServer(
                        TogglePayload.REQUEST_ID,
                        new PacketByteBuf(Unpooled.buffer())
                );
            }

            while (TOGGLE_KEY.wasPressed()) {
                if (client.currentScreen == null
                        && NetworkManager.canServerReceive(TogglePayload.ID)) {
                    NetworkManager.sendToServer(
                            TogglePayload.ID,
                            new PacketByteBuf(Unpooled.buffer())
                    );
                }
            }

            if (ToggleClientState.isDisabled()) {
                // Better Combat checks this field in doAttack/handleBlockBreaking/doItemUse.
                BetterCombatClient.ENABLED = false;
                if (!suppressingBetterCombat) {
                    suppressingBetterCombat = true;
                    ((MinecraftClient_BetterCombat) client).cancelUpswing();
                }
            } else if (suppressingBetterCombat) {
                suppressingBetterCombat = false;
                BetterCombatClient.ENABLED = true;
            }
        });

        ToggleBetterCombat.LOGGER.info(
                "Toggle Better Combat client loaded (key: {})",
                TOGGLE_KEY.getBoundKeyTranslationKey()
        );
    }
}
