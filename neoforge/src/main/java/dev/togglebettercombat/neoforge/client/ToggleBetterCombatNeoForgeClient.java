package dev.togglebettercombat.neoforge.client;

import dev.togglebettercombat.ToggleBetterCombat;
import dev.togglebettercombat.client.ToggleBetterCombatClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ToggleBetterCombat.MOD_ID, dist = Dist.CLIENT)
public final class ToggleBetterCombatNeoForgeClient {
    public ToggleBetterCombatNeoForgeClient() {
        ToggleBetterCombatClient.init();
    }
}
