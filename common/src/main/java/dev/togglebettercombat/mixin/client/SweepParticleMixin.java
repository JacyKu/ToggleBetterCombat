package dev.togglebettercombat.mixin.client;

import net.bettercombat.api.CombatFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Better Combat's client config suppresses vanilla sweep particles globally
 * ({@code isSweepingParticleEnabled = false} by default), even while its attack
 * handling is disabled. Spawn the sweep particle ourselves before Better
 * Combat's cancellation runs, so vanilla sweeping looks vanilla again.
 *
 * <p>Priority 1 applies after Better Combat's default-priority HEAD injection,
 * which makes this injection run first.</p>
 */
@Mixin(value = ClientPlayNetworkHandler.class, priority = 1)
public abstract class SweepParticleMixin {
    @Inject(method = "onParticle", at = @At("HEAD"), cancellable = true)
    private void togglebettercombat$spawnVanillaSweepParticle(
            ParticleS2CPacket packet,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return;
        }
        if (!CombatFlags.isAttackDisabled(client.player)) {
            return;
        }
        if (!packet.getParameters().getType().equals(ParticleTypes.SWEEP_ATTACK)) {
            return;
        }

        // Mirrors the count == 0 branch of vanilla ClientPlayNetworkHandler#onParticle.
        double velocityX = packet.getSpeed() * packet.getOffsetX();
        double velocityY = packet.getSpeed() * packet.getOffsetY();
        double velocityZ = packet.getSpeed() * packet.getOffsetZ();

        client.world.addParticle(
                packet.getParameters(),
                packet.isLongDistance(),
                packet.getX(),
                packet.getY(),
                packet.getZ(),
                velocityX,
                velocityY,
                velocityZ
        );

        ci.cancel();
    }
}
