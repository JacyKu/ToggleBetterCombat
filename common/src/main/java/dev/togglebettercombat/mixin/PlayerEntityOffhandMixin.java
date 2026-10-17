package dev.togglebettercombat.mixin;

import net.bettercombat.api.CombatFlags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Better Combat hides the offhand while holding a two-handed weapon. When the
 * toggle is off, the player should get vanilla behavior (the offhand visible
 * and usable).
 *
 * <p>Priority 1 applies this injection after Better Combat's priority-899 HEAD
 * injection, which makes this one run first and prevents its cancellation.</p>
 */
@Mixin(value = PlayerEntity.class, priority = 1)
public abstract class PlayerEntityOffhandMixin {
    @Inject(method = "getEquippedStack", at = @At("HEAD"), cancellable = true)
    private void togglebettercombat$restoreOffhand(
            EquipmentSlot slot,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (slot != EquipmentSlot.OFFHAND) {
            return;
        }

        PlayerEntity player = (PlayerEntity) (Object) this;
        if (CombatFlags.isAttackDisabled(player)) {
            cir.setReturnValue(player.getInventory().getStack(
                    EquipmentSlot.OFFHAND.getOffsetEntitySlotId(0)
            ));
        }
    }
}
