package dev.togglebettercombat;

import net.bettercombat.api.CombatFlags;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Server-authoritative toggle state.
 *
 * <p>Better Combat already has a per-player "attacks disabled" flag that it
 * syncs to every tracking client. It is normally driven by the vanilla
 * {@code bettercombat_disabled} command tag, which is stored in the player's
 * NBT and therefore survives relogging. We use that tag as our source of
 * truth, which gives us persistence without adding a config or a separate
 * data file.</p>
 */
public final class ToggleService {
    private ToggleService() {
    }

    public static boolean isDisabled(ServerPlayerEntity player) {
        return player.getCommandTags().contains(CombatFlags.DISABLED_TAG);
    }

    public static void toggle(ServerPlayerEntity player) {
        boolean disabled = !isDisabled(player);
        setDisabled(player, disabled);
        ToggleNotifier.send(player, !CombatFlags.isAttackDisabled(player));
    }

    public static void setDisabled(ServerPlayerEntity player, boolean disabled) {
        if (disabled) {
            player.addCommandTag(CombatFlags.DISABLED_TAG);
            CombatFlags.set(player, (byte) (CombatFlags.get(player) | CombatFlags.TAG_DISABLED));
        } else {
            player.removeCommandTag(CombatFlags.DISABLED_TAG);
            CombatFlags.set(player, (byte) (CombatFlags.get(player) & ~CombatFlags.TAG_DISABLED));
        }
    }

    /**
     * Re-applies the persistent tag on join, before Better Combat's per-tick
     * tag mirror has a chance to run.
     */
    public static void applySavedState(ServerPlayerEntity player) {
        if (isDisabled(player)) {
            setDisabled(player, true);
        }
    }

    /**
     * Restores the toggle on the freshly created player entity after a respawn.
     * If the tag already survived, this is a no-op.
     */
    public static void copyState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (isDisabled(oldPlayer)) {
            setDisabled(newPlayer, true);
        }
    }
}
