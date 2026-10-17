package dev.togglebettercombat.network;

import dev.togglebettercombat.ToggleBetterCombat;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * S2C: the server tells a modded client that the toggle changed. The client
 * chooses action bar or toast based on its own client config.
 */
public record ToggleStatePayload(boolean enabled) implements CustomPayload {
    public static final CustomPayload.Id<ToggleStatePayload> ID =
            new CustomPayload.Id<>(Identifier.of(ToggleBetterCombat.MOD_ID, "state"));

    public static final PacketCodec<RegistryByteBuf, ToggleStatePayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> buf.writeBoolean(payload.enabled()),
                    buf -> new ToggleStatePayload(buf.readBoolean())
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
