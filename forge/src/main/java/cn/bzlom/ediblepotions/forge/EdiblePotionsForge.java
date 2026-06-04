package cn.bzlom.ediblepotions.forge;

import cn.bzlom.ediblepotions.EdiblePotionsMod;
import cn.bzlom.ediblepotions.client.screen.InfusionTableScreen; // 导入 Screen
import cn.bzlom.ediblepotions.registry.ModMenuTypes; // 导入 MenuType
import cn.bzlom.ediblepotions.forge.datagen.ModBlockStateProvider;
import cn.bzlom.ediblepotions.forge.datagen.ModItemModelProvider;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.menu.MenuRegistry; // 关键导入：Architectury 的菜单注册器
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent; // 关键导入：客户端设置事件
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EdiblePotionsMod.MOD_ID)
public final class EdiblePotionsForge {
    public EdiblePotionsForge(FMLJavaModLoadingContext context) {
        // 1. 注册 Architectury 的事件总线
        EventBuses.registerModEventBus(EdiblePotionsMod.MOD_ID, context.getModEventBus());

        // 2. 注册 DataGen 事件监听器 (生成模型/材质)
        context.getModEventBus().addListener(this::gatherData);

        // 3. 注册客户端初始化监听器 (绑定 GUI 界面) —— 【新增】
        context.getModEventBus().addListener(this::clientSetup);

        // 4. 初始化 Common 逻辑
        EdiblePotionsMod.init();
    }

    // 数据生成逻辑 (保持不变)
    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeClient(),
                new ModItemModelProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper())
        );
        event.getGenerator().addProvider(
                event.includeClient(),
                new ModBlockStateProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper())
        );
    }

    // 客户端初始化逻辑 (新增)
    private void clientSetup(final FMLClientSetupEvent event) {
        // 使用 Architectury 的 MenuRegistry 绑定 Menu 和 Screen
        // enqueueWork 确保代码在主线程安全执行，防止多线程崩溃
        event.enqueueWork(() -> {
            MenuRegistry.registerScreenFactory(ModMenuTypes.INFUSION_TABLE_MENU.get(), InfusionTableScreen::new);
        });
    }
}