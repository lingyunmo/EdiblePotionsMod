package cn.bzlom.registry;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.menu.InfusionTableMenu;
import dev.architectury.registry.menu.MenuRegistry; // 关键导入！
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.MENU);

    // 【核心修复】
    // 使用 MenuRegistry.ofExtended 而不是 new MenuType
    // 这样它就能自动识别你的 (int, Inventory, FriendlyByteBuf) 构造函数了
    public static final RegistrySupplier<MenuType<InfusionTableMenu>> INFUSION_TABLE_MENU = MENUS.register("infusion_table_menu",
            () -> MenuRegistry.ofExtended(InfusionTableMenu::new));

    public static void register() {
        MENUS.register();
    }
}