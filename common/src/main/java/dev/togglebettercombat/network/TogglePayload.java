package dev.togglebettercombat.network;

import dev.togglebettercombat.ToggleBetterCombat;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Empty C2S payload: the server flips the sender's toggle. No state is sent
 * from the client, so a modified client cannot force a particular state.
 */
public record TogglePayload() implements CustomPayload {
    public static final TogglePayload INSTANCE = new TogglePayload();

    public static final CustomPayload.Id<TogglePayload> ID =
            new CustomPayload.Id<>(Identifier.of(ToggleBetterCombat.MOD_ID, "toggle"));

    public static final PacketCodec<RegistryByteBuf, TogglePayload> CODEC =
            PacketCodec.unit(INSTANCE);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
