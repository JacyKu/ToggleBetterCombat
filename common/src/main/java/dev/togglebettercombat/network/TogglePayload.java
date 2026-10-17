package dev.togglebettercombat.network;

import dev.togglebettercombat.ToggleBetterCombat;
import net.minecraft.util.Identifier;

public final class TogglePayload {
    public static final Identifier ID = new Identifier(ToggleBetterCombat.MOD_ID, "toggle");
    public static final Identifier REQUEST_ID = new Identifier(ToggleBetterCombat.MOD_ID, "request_state");

    private TogglePayload() {
    }
}
