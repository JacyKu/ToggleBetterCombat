package dev.togglebettercombat.fabric.client;

import dev.togglebettercombat.client.ToggleBetterCombatClient;
import net.fabricmc.api.ClientModInitializer;

public final class ToggleBetterCombatFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToggleBetterCombatClient.init();
    }
}
