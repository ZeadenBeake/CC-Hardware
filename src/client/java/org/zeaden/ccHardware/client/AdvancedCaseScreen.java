package org.zeaden.ccHardware.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.zeaden.ccHardware.menu.AdvancedCaseMenu;

public class AdvancedCaseScreen extends AbstractContainerScreen<AdvancedCaseMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public AdvancedCaseScreen(AdvancedCaseMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "CPU", 77, 10, 0x404040, false);
        graphics.drawString(font, "RAM", 48, 30, 0x404040, false);
        graphics.drawString(font, "Storage", 60, 58, 0x404040, false);
        graphics.drawString(font, "Floppy", 110, 30, 0x404040, false);
        super.renderLabels(graphics, mouseX, mouseY);
    }
}