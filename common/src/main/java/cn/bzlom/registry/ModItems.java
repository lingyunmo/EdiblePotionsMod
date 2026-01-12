package cn.bzlom.registry;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.item.InfusedFoodItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions; // 确保导入

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.ITEM);

    // ---------------------------------------------------------------
    // 1.20.1 全量食物列表 (已移除导致崩溃的蛋糕、奶桶及1.21的不祥之瓶)
    // ---------------------------------------------------------------
    private static final Item[] SUPPORTED_FOODS = {
            // --- 基础水果/蔬菜 ---
            Items.APPLE,                // 苹果
            Items.GOLDEN_APPLE,         // 金苹果
            Items.ENCHANTED_GOLDEN_APPLE, // 附魔金苹果
            Items.MELON_SLICE,          // 西瓜片
            Items.SWEET_BERRIES,        // 甜浆果
            Items.GLOW_BERRIES,         // 发光浆果 (1.17加入，1.20.1可用)
            Items.CHORUS_FRUIT,         // 紫颂果
            Items.CARROT,               // 胡萝卜
            Items.GOLDEN_CARROT,        // 金胡萝卜
            Items.POTATO,               // 马铃薯
            Items.BAKED_POTATO,         // 烤马铃薯
            Items.POISONOUS_POTATO,     // 毒马铃薯
            Items.BEETROOT,             // 甜菜根
            Items.DRIED_KELP,           // 干海带

            // --- 肉类 (生/熟) ---
            Items.BEEF,                 // 生牛肉
            Items.COOKED_BEEF,          // 牛排
            Items.PORKCHOP,             // 生猪排
            Items.COOKED_PORKCHOP,      // 熟猪排
            Items.MUTTON,               // 生羊肉
            Items.COOKED_MUTTON,        // 熟羊肉
            Items.CHICKEN,              // 生鸡肉
            Items.COOKED_CHICKEN,       // 熟鸡肉
            Items.RABBIT,               // 生兔肉
            Items.COOKED_RABBIT,        // 熟兔肉
            Items.ROTTEN_FLESH,         // 腐肉

            // --- 鱼类 ---
            Items.COD,                  // 生鳕鱼
            Items.COOKED_COD,           // 熟鳕鱼
            Items.SALMON,               // 生鲑鱼
            Items.COOKED_SALMON,        // 熟鲑鱼
            Items.TROPICAL_FISH,        // 热带鱼
            Items.PUFFERFISH,           // 河豚

            // --- 面食/烘焙 ---
            Items.BREAD,                // 面包
            Items.COOKIE,               // 曲奇
            Items.PUMPKIN_PIE,          // 南瓜派

            // --- 汤/炖菜/瓶装 ---
            Items.MUSHROOM_STEW,        // 蘑菇煲
            Items.BEETROOT_SOUP,        // 甜菜汤
            Items.RABBIT_STEW,          // 兔肉煲
            Items.SUSPICIOUS_STEW,      // 谜之炖菜 (注：这本身就带效果，注能后会叠加！)
            Items.HONEY_BOTTLE,         // 蜂蜜瓶 (这个有 FoodProperties，安全)

            // --- 其他 ---
            Items.SPIDER_EYE            // 蜘蛛眼
    };

    public static final List<RegistrySupplier<Item>> INFUSED_ITEMS = new ArrayList<>();

    public static void register() {
        for (Item food : SUPPORTED_FOODS) {
            // 获取食物 ID (e.g., "apple")
            String foodName = BuiltInRegistries.ITEM.getKey(food).getPath();

            for (Potion potion : BuiltInRegistries.POTION) {
                // 必须过滤掉没有效果的药水 (如 Water, Thick, Awkward)
                if (potion.getEffects().isEmpty()) continue;

                String potionKey = BuiltInRegistries.POTION.getKey(potion).getPath();

                // 生成 ID: "apple_night_vision"
                String id = foodName + "_" + potionKey;

                RegistrySupplier<Item> itemSupplier = ITEMS.register(id, () ->
                        new InfusedFoodItem(food, potion));

                INFUSED_ITEMS.add(itemSupplier);
            }
        }
        ITEMS.register();
    }
}