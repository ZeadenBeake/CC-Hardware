package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.zeaden.ccHardware.blocks.KeyboardBlockEntity;

public record KeyInputPacket(BlockPos pos, int keyCode, boolean held, boolean isChar, String character) implements FabricPacket {

    public static final PacketType<KeyInputPacket> TYPE = PacketType.create(
            new ResourceLocation(CcHardware.MOD_ID, "key_input"),
            buf -> new KeyInputPacket(
                    buf.readBlockPos(),
                    buf.readInt(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readUtf()
            )
    );

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(keyCode);
        buf.writeBoolean(held);
        buf.writeBoolean(isChar);
        buf.writeUtf(character);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (packet, player, responseSender) -> {
            player.server.execute(() -> {
                var level = player.level();
                if (level.getBlockEntity(packet.pos()) instanceof KeyboardBlockEntity kb) {
                    if (packet.isChar()) {
                        kb.getPeripheral().sendChar(packet.character());
                    } else if (packet.held()) {
                        kb.getPeripheral().sendKeyUp(packet.keyCode());
                    } else {
                        kb.getPeripheral().sendKey(packet.keyCode(), false);
                    }
                }
            });
        });
    }
}