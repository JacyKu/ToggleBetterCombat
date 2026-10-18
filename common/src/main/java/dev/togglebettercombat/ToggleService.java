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
     * Re-applies the state on join. Legacy tags from older builds are migrated
     * once per player, so later admin tags are left alone.
     */
    public static void applySavedState(ServerPlayerEntity player) {
        TogglePlayerNbt state = (TogglePlayerNbt) player;
        if (!state.togglebettercombat$isMigrated()) {
            boolean legacy = player.getCommandTags().remove(LEGACY_TAG);
            legacy |= player.getCommandTags().remove(LEGACY_BC_TAG);
            if (legacy) {
                state.togglebettercombat$setDisabled(true);
            }
            state.togglebettercombat$setMigrated(true);
        }
        CombatFlags.setAttacksDisabled(player, isDisabled(player));
    }

    /**
     * Restores the toggle on the freshly created player entity after a respawn.
     */
    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        TogglePlayerNbt oldState = (TogglePlayerNbt) oldPlayer;
        TogglePlayerNbt newState = (TogglePlayerNbt) newPlayer;
        newState.togglebettercombat$setDisabled(oldState.togglebettercombat$isDisabled());
        newState.togglebettercombat$setMigrated(oldState.togglebettercombat$isMigrated());
        CombatFlags.setAttacksDisabled(newPlayer, isDisabled(newPlayer));
    }
}
