package org.zeaden.ccHardware.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.zeaden.ccHardware.KeyInputPacket;

public class KeyboardScreen extends Screen {

    private final BlockPos keyboardPos;

    public KeyboardScreen(BlockPos keyboardPos) {
        super(Component.literal(""));
        this.keyboardPos = keyboardPos;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // Draw a subtle indicator so the player knows they're in keyboard mode
        graphics.drawCenteredString(font, "Keyboard active - Press ESC to exit", width / 2, height - 40, 0xAAAAAA);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose();
            return true;
        }
        ClientPlayNetworking.send(new KeyInputPacket(keyboardPos, keyCode, false, false, ""));
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) return true;
        ClientPlayNetworking.send(new KeyInputPacket(keyboardPos, keyCode, true, false, ""));
        return true;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        ClientPlayNetworking.send(new KeyInputPacket(keyboardPos, 0, false, true, String.valueOf(chr)));
        return true;
    }
}