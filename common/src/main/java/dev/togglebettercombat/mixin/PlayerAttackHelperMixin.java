package dev.togglebettercombat.mixin;

import net.bettercombat.api.AttackHand;
import net.bettercombat.api.CombatFlags;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.Pose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes Better Combat's per-player logic fall back to vanilla while the toggle
 * is off.
 *
 * <p>Sweeping: Better Combat suppresses vanilla sweeping for any weapon with
 * Better Combat attributes (see {@code PlayerEntityMixin#disableSweeping}). That
 * check does not look at {@link CombatFlags}, so toggling attacks off alone
 * would leave vanilla Sweeping Edge broken. Returning {@code null} makes Better
 * Combat's {@code PlayerEntity#attack} mixins fall back to vanilla hand/sweep
 * handling.</p>
 *
 * <p>Dual wielding: returning {@code false} makes Better Combat remove its
 * dual-wield attack-speed modifier on the next tick, and prevents it from
 * attacking with the offhand.</p>
 *
 * <p>Poses: an empty pose restores the vanilla held-item pose.</p>
 */
@Mixin(value = PlayerAttackHelper.class, priority = 2000)
public abstract class PlayerAttackHelperMixin {
    @Inject(method = "getCurrentAttack", at = @At("HEAD"), cancellable = true)
    private static void togglebettercombat$allowVanillaSweeping(
            PlayerEntity player,
            int comboCount,
            CallbackInfoReturnable<AttackHand> cir
    ) {
        if (CombatFlags.isAttackDisabled(player)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "isDualWielding(Lnet/minecraft/entity/player/PlayerEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void togglebettercombat$disableDualWielding(
            PlayerEntity player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (CombatFlags.isAttackDisabled(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "poseForPlayer", at = @At("HEAD"), cancellable = true)
    private static void togglebettercombat$useVanillaPose(
            PlayerEntity player,
            CallbackInfoReturnable<Pose> cir
    ) {
        if (CombatFlags.isAttackDisabled(player)) {
            cir.setReturnValue(new Pose("", ""));
        }
    }
}
