package dev.togglebettercombat.mixin;

import dev.togglebettercombat.TogglePlayerNbt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores the toggle in the player's own NBT. No command tags, so uninstalling
 * the mod leaves only an inert NBT key behind.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerNbtMixin implements TogglePlayerNbt {
    @Unique
    private boolean togglebettercombat$disabled;

    @Override
    public boolean togglebettercombat$isDisabled() {
        return togglebettercombat$disabled;
    }

    @Override
    public void togglebettercombat$setDisabled(boolean disabled) {
        togglebettercombat$disabled = disabled;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void togglebettercombat$read(NbtCompound nbt, CallbackInfo ci) {
        togglebettercombat$disabled = nbt.getBoolean(TogglePlayerNbt.NBT_KEY);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void togglebettercombat$write(NbtCompound nbt, CallbackInfo ci) {
        if (togglebettercombat$disabled) {
            nbt.putBoolean(TogglePlayerNbt.NBT_KEY, true);
        } else {
            nbt.remove(TogglePlayerNbt.NBT_KEY);
        }
    }
}
