package cn.bzlom.client.screen;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.menu.InfusionTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InfusionTableScreen extends AbstractContainerScreen<InfusionTableMenu> {
    // 指向背景图路径
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EdiblePotionsMod.MOD_ID, "textures/gui/infusion_table.png");

    public InfusionTableScreen(InfusionTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // 设置标题位置 (左上角 x, y)
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    // 1. 渲染整个屏幕 (Render Loop)
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics); // 先把背景压暗
        super.render(guiGraphics, mouseX, mouseY, partialTick); // 渲染背景图和槽位
        this.renderTooltip(guiGraphics, mouseX, mouseY); // 渲染鼠标悬停提示 (Tooltips)
    }

    // 2. 渲染背景图 (Draw Texture)
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // 计算屏幕中心位置
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // 绘制背景底图
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 绘制工作进度条 (箭头)
        renderProgressArrow(guiGraphics, x, y);
    }

    // 辅助方法：绘制进度箭头
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            // 调用 Menu 里的计算方法
            int arrowWidth = menu.getScaledProgress();

            // 绘制箭头
            // Texture X: 176 (对应贴图里满状态箭头的起始位置)
            // Texture Y: 14  (对应贴图里满状态箭头的起始位置)
            // Screen X: x + 81 (原版是79，右移了2像素)
            // Screen Y: y + 35 (原版高度)
            guiGraphics.blit(TEXTURE, x + 81, y + 35, 176, 14, arrowWidth, 16);
        }
    }
}