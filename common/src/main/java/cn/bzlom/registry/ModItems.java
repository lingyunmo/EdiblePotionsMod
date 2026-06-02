package cn.bzlom.registry;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.item.InfusedFoodItem;
import cn.bzlom.item.InfusedFoodItem.Delivery;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.ITEM);

    private static final Item[] SUPPORTED_FOODS = {
            Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE,
            Items.MELON_SLICE, Items.SWEET_BERRIES, Items.GLOW_BERRIES,
            Items.CHORUS_FRUIT, Items.CARROT, Items.GOLDEN_CARROT,
            Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO,
            Items.BEETROOT, Items.DRIED_KELP,
            Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP,
            Items.MUTTON, Items.COOKED_MUTTON, Items.CHICKEN, Items.COOKED_CHICKEN,
            Items.RABBIT, Items.COOKED_RABBIT, Items.ROTTEN_FLESH,
            Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON,
            Items.TROPICAL_FISH, Items.PUFFERFISH,
            Items.BREAD, Items.COOKIE, Items.PUMPKIN_PIE,
            Items.MUSHROOM_STEW, Items.BEETROOT_SOUP, Items.RABBIT_STEW,
            Items.SUSPICIOUS_STEW, Items.HONEY_BOTTLE,
            Items.SPIDER_EYE
    };

    /** 所有已注册的注能食物 (跨 3 种递送方式) */
    public static final List<RegistrySupplier<Item>> INFUSED_ITEMS = new ArrayList<>();

    /** 递送方式 → 注册 ID 后缀 */
    private static final Delivery[] DELIVERIES = Delivery.values();

    public static void register() {
        for (Item food : SUPPORTED_FOODS) {
            String foodName = BuiltInRegistries.ITEM.getKey(food).getPath();

            for (Potion potion : BuiltInRegistries.POTION) {
                if (potion.getEffects().isEmpty()) continue;
                String potionKey = BuiltInRegistries.POTION.getKey(potion).getPath();

                for (Delivery delivery : DELIVERIES) {
                    // ID: "apple_night_vision", "apple_night_vision_splash", "apple_night_vision_lingering"
                    String id = foodName + "_" + potionKey + deliverySuffix(delivery);
                    RegistrySupplier<Item> supplier = ITEMS.register(id, () ->
                            new InfusedFoodItem(food, potion, delivery));
                    INFUSED_ITEMS.add(supplier);
                }
            }
        }
        ITEMS.register();
    }

    /** 根据递送方式返回注册 ID 后缀 */
    public static String deliverySuffix(Delivery delivery) {
        return switch (delivery) {
            case SPLASH -> "_splash";
            case LINGERING -> "_lingering";
            case REGULAR -> "";
        };
    }
}
