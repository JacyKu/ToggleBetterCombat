package dev.togglebettercombat;

import net.bettercombat.api.CombatFlags;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Server-authoritative toggle state, stored in the player's NBT.
 *
 * <p>No command tags are used during normal operation, so uninstalling this
 * mod leaves only an inert NBT key and Better Combat works normally.</p>
 */
public final class ToggleService {
    private static final String LEGACY_TAG = "togglebettercombat_disabled";
    private static final String LEGACY_BC_TAG = CombatFlags.DISABLED_TAG;

    private ToggleService() {
    }

    public static boolean isDisabled(ServerPlayerEntity player) {
        return ((TogglePlayerNbt) player).togglebettercombat$isDisabled();
    }

    public static void toggle(ServerPlayerEntity player) {
        boolean disabled = !isDisabled(player);
        setDisabled(player, disabled);
        ToggleNotifier.send(player, !CombatFlags.isAttackDisabled(player));
    }

    public static void setDisabled(ServerPlayerEntity player, boolean disabled) {
        ((TogglePlayerNbt) player).togglebettercombat$setDisabled(disabled);
        // Runtime-only BC flag; not persisted by Better Combat.
        CombatFlags.setAttacksDisabled(player, disabled);
    }

    /**
     * Re-applies the state on join and migrates the tags used by older builds.
     */
    public static void applySavedState(ServerPlayerEntity player) {
        boolean legacy = player.getCommandTags().remove(LEGACY_TAG);
        legacy |= player.getCommandTags().remove(LEGACY_BC_TAG);
        if (legacy) {
            ((TogglePlayerNbt) player).togglebettercombat$setDisabled(true);
        }
        CombatFlags.setAttacksDisabled(player, isDisabled(player));
    }

    /**
     * Restores the toggle on the freshly created player entity after a respawn.
     */
    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (isDisabled(oldPlayer)) {
            setDisabled(newPlayer, true);
        }
    }
}
