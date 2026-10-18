package dev.togglebettercombat;

import net.bettercombat.api.CombatFlags;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Server-authoritative toggle state.
 *
 * <p>We keep our own command tag instead of Better Combat's
 * {@code bettercombat_disabled} tag. That is important for uninstall safety:
 * if this mod is removed, Better Combat no longer sees a disable tag and works
 * normally again.</p>
 */
public final class ToggleService {
    public static final String TAG = "togglebettercombat_disabled";
    private static final String LEGACY_TAG = CombatFlags.DISABLED_TAG;

    private ToggleService() {
    }

    public static boolean isDisabled(ServerPlayerEntity player) {
        return player.getCommandTags().contains(TAG);
    }

    public static void toggle(ServerPlayerEntity player) {
        boolean disabled = !isDisabled(player);
        setDisabled(player, disabled);
        ToggleNotifier.send(player, !CombatFlags.isAttackDisabled(player));
    }

    public static void setDisabled(ServerPlayerEntity player, boolean disabled) {
        if (disabled) {
            player.addCommandTag(TAG);
        } else {
            player.removeCommandTag(TAG);
        }
        // Runtime-only BC flag; not persisted by Better Combat.
        CombatFlags.setAttacksDisabled(player, disabled);
    }

    /**
     * Re-applies the state on join and migrates the old
     * {@code bettercombat_disabled} tag used by earlier builds.
     */
    public static void applySavedState(ServerPlayerEntity player) {
        if (player.getCommandTags().contains(LEGACY_TAG)
                && !player.getCommandTags().contains(TAG)) {
            player.getCommandTags().remove(LEGACY_TAG);
            player.addCommandTag(TAG);
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
