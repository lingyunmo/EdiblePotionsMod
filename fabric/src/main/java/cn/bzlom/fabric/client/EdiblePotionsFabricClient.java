package cn.bzlom.fabric.client;

import cn.bzlom.client.screen.InfusionTableScreen; // 导入 Screen
import cn.bzlom.registry.ModMenuTypes; // 导入 MenuType
import dev.architectury.registry.menu.MenuRegistry; // Architectury 神器
import net.fabricmc.api.ClientModInitializer;

public class EdiblePotionsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 绑定 Screen 到 Menu
        MenuRegistry.registerScreenFactory(ModMenuTypes.INFUSION_TABLE_MENU.get(), InfusionTableScreen::new);
    }
}