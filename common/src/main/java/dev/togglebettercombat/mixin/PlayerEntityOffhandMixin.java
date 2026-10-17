package dev.togglebettercombat.mixin;

import dev.togglebettercombat.ToggleState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Restores the offhand for toggled-off players, bypassing Better Combat's
 * two-handed weapon restriction.
 *
 * <p>Priority 1 applies this injection after Better Combat's default-priority
 * HEAD injection, so this one runs first and prevents its cancellation.</p>
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
        if (ToggleState.isDisabled(player)) {
            cir.setReturnValue(player.getInventory().getStack(
                    EquipmentSlot.OFFHAND.getOffsetEntitySlotId(0)
            ));
        }
    }
}
