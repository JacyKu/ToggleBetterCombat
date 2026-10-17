package dev.togglebettercombat.forge;

import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.client.ToggleBetterCombatClient;
import dev.togglebettercombat.client.ToggleConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ToggleBetterCombat.MOD_ID)
public final class ToggleBetterCombatForge {
    public ToggleBetterCombatForge() {
        ToggleBetterCombat.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ToggleBetterCombatClient.init();
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (minecraft, parent) -> new ToggleConfigScreen(parent)
                    )
            );
        });
    }
}
