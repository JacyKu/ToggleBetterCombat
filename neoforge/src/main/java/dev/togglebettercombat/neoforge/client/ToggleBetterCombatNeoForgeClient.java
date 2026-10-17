package dev.togglebettercombat.neoforge.client;

import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.client.ToggleBetterCombatClient;
import dev.togglebettercombat.client.ToggleConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ToggleBetterCombat.MOD_ID, dist = Dist.CLIENT)
public final class ToggleBetterCombatNeoForgeClient {
    public ToggleBetterCombatNeoForgeClient() {
        ToggleBetterCombatClient.init();

        // Adds the config button to NeoForge's mods screen.
        ModList.get().getModContainerById(ToggleBetterCombat.MOD_ID).ifPresent(container ->
                container.registerExtensionPoint(
                        IConfigScreenFactory.class,
                        (mod, parent) -> new ToggleConfigScreen(parent)
                )
        );
    }
}
