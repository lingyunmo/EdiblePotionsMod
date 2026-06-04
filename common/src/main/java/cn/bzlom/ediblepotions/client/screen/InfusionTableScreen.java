package cn.bzlom.ediblepotions.client.screen;

import cn.bzlom.ediblepotions.EdiblePotionsMod;
import cn.bzlom.ediblepotions.menu.InfusionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InfusionTableScreen extends AbstractContainerScreen<InfusionTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EdiblePotionsMod.MOD_ID, "textures/gui/infusion_table.png");

    public InfusionTableScreen(InfusionTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // GuiGraphics.blit 在 1.20.1 中内部处理 shader 设置，无需手动 RenderSystem 调用
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int arrowWidth = menu.getScaledProgress();
            guiGraphics.blit(TEXTURE, x + 81, y + 35, 176, 14, arrowWidth, 16);
        }
    }
}
