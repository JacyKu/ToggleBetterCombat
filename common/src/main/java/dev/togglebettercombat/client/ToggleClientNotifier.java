package dev.togglebettercombat.client;

import dev.togglebettercombat.ToggleText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

/**
 * Client-side rendering of the toggle feedback.
 */
public final class ToggleClientNotifier {
    private ToggleClientNotifier() {
    }

    public static void show(boolean enabled) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        Text message = ToggleText.message(enabled);

        if (ToggleClientConfig.notificationStyle() == ToggleClientConfig.NotificationStyle.TOAST) {
            client.getToastManager().add(SystemToast.create(
                    client,
                    SystemToast.Type.PERIODIC_NOTIFICATION,
                    ToggleText.toastTitle(),
                    message
            ));
        } else {
            client.inGameHud.setOverlayMessage(message, false);
        }
    }
}
