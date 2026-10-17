package dev.togglebettercombat.network;

import dev.togglebettercombat.ToggleBetterCombat;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class ToggleStatePayload {
    public static final Identifier ID = new Identifier(ToggleBetterCombat.MOD_ID, "state");

    private ToggleStatePayload() {
    }

    public static PacketByteBuf write(boolean disabled, boolean notify) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(disabled);
        buf.writeBoolean(notify);
        return buf;
    }
}
