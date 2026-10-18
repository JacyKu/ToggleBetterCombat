package dev.togglebettercombat.forge;

import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.forge.client.ToggleBetterCombatForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ToggleBetterCombat.MOD_ID)
public final class ToggleBetterCombatForge {
    public ToggleBetterCombatForge() {
        ToggleBetterCombat.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ToggleBetterCombatForgeClient::init);
    }
}
