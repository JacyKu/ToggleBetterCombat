package dev.togglebettercombat.mixin;

import dev.togglebettercombat.ToggleState;
import net.bettercombat.api.AttackHand;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Falls back to vanilla attack/sweep handling and disables Better Combat's
 * dual-wield speed bonus for toggled-off players.
 */
@Mixin(value = PlayerAttackHelper.class, priority = 2000)
public abstract class PlayerAttackHelperMixin {
    @Inject(method = "getCurrentAttack", at = @At("HEAD"), cancellable = true)
    private static void togglebettercombat$disableAttack(
            PlayerEntity player,
            int comboCount,
            CallbackInfoReturnable<AttackHand> cir
    ) {
        if (ToggleState.isDisabled(player)) {
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
        if (ToggleState.isDisabled(player)) {
            cir.setReturnValue(false);
        }
    }
}
