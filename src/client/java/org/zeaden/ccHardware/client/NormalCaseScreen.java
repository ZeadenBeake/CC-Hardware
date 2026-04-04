package org.zeaden.ccHardware.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.zeaden.ccHardware.menu.NormalCaseMenu;

public class NormalCaseScreen extends AbstractContainerScreen<NormalCaseMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    public NormalCaseScreen(NormalCaseMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // Draw background using generic inventory texture
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // Column headers
        graphics.drawString(font, "CPU", 77, 10, 0x404040, false);
        graphics.drawString(font, "RAM", 57, 40, 0x404040, false);
        graphics.drawString(font, "Storage", 82, 40, 0x404040, false);
        // Player inventory label
        super.renderLabels(graphics, mouseX, mouseY);
    }
}