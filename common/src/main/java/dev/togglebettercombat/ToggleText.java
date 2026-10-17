package dev.togglebettercombat;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Shared message text, used by both the server fallback and the client notifier.
 */
public final class ToggleText {
    private ToggleText() {
    }

    public static Text message(boolean enabled) {
        return Text.translatable(
                enabled
                        ? "message.togglebettercombat.enabled"
                        : "message.togglebettercombat.disabled"
        ).formatted(enabled ? Formatting.GREEN : Formatting.GOLD);
    }

    public static Text toastTitle() {
        return Text.translatable("toast.togglebettercombat.title");
    }
}
