package cn.bzlom.registry;

import cn.bzlom.EdiblePotionsMod;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeTabs {
    // 创建页签的注册器
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MAIN_TAB = TABS.register("main", () ->
            CreativeTabRegistry.create(builder -> {
                // 1. 设置标题
                builder.title(Component.translatable("itemGroup." + EdiblePotionsMod.MOD_ID + ".main"));

                // 2. 设置图标
                builder.icon(() -> {
                    if (ModItems.INFUSED_ITEMS.isEmpty()) return new ItemStack(Items.APPLE);
                    // 拿第一个生成的物品做图标
                    return new ItemStack(ModItems.INFUSED_ITEMS.get(0).get());
                });

                // 3. 填充物品
                builder.displayItems((features, output) -> {
                    // 遍历生成的那个大列表，一个一个加进去
                    for (var itemSupplier : ModItems.INFUSED_ITEMS) {
                        output.accept(itemSupplier.get());
                    }
                });
            })
    );

    public static void register() {
        TABS.register();
    }
}