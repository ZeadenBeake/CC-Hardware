package org.zeaden.ccHardware;

import net.minecraft.core.BlockPos;
import java.util.function.Consumer;

public class ClientProxy {
    private static Consumer<BlockPos> keyboardScreenOpener = pos -> {};

    public static void setKeyboardScreenOpener(Consumer<BlockPos> opener) {
        keyboardScreenOpener = opener;
    }

    public static void openKeyboardScreen(BlockPos pos) {
        keyboardScreenOpener.accept(pos);
    }
}