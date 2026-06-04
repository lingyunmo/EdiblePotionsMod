package cn.bzlom.ediblepotions.forge.datagen;

import cn.bzlom.ediblepotions.EdiblePotionsMod;
import cn.bzlom.ediblepotions.item.InfusedFoodItem;
import cn.bzlom.ediblepotions.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EdiblePotionsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var itemSupplier : ModItems.INFUSED_ITEMS) {
            Item item = itemSupplier.get();

            if (item instanceof InfusedFoodItem infusedFood) {
                // 1. 获取自定义物品的 ID (ediblepotions:apple_speed)
                String path = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();

                // 2. 获取原版基础食物的 ID (minecraft:apple)
                ResourceLocation baseFoodId = ForgeRegistries.ITEMS.getKey(infusedFood.getBaseFood());

                if (baseFoodId != null) {
                    // 默认情况下，贴图路径是 "item/apple"
                    String texturePath = "item/" + baseFoodId.getPath();

                    // 特殊情况：附魔金苹果用金苹果的
                    if (infusedFood.getBaseFood() == Items.ENCHANTED_GOLDEN_APPLE) {
                        texturePath = "item/golden_apple";
                    }

                    // ---【生成 JSON】---
                    getBuilder(path)
                            .parent(getExistingFile(mcLoc("item/generated")))
                            .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                                    baseFoodId.getNamespace(),
                                    texturePath // 使用修正后的路径
                            ));
                }
            }
        }
    }
}