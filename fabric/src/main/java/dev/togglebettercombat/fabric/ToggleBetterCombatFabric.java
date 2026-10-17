package dev.togglebettercombat.fabric;

import dev.togglebettercombat.ToggleBetterCombat;
import net.fabricmc.api.ModInitializer;

public final class ToggleBetterCombatFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ToggleBetterCombat.init();
    }
}
