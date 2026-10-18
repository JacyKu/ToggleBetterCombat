package dev.togglebettercombat.forge.client;

import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.client.ToggleBetterCombatClient;
import dev.togglebettercombat.client.ToggleConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public final class ToggleBetterCombatForgeClient {
    private ToggleBetterCombatForgeClient() {
    }

    public static void init() {
        ToggleBetterCombatClient.init();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new ToggleConfigScreen(parent)
                )
        );
    }
}
