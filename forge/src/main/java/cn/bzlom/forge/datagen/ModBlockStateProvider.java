package cn.bzlom.forge.datagen;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EdiblePotionsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // 简单生成一个六面同色的方块
        // 需要你在 resources/assets/ediblepotions/textures/block/ 下放一张 infusion_table.png
        // 或者我们暂时借用原版 铁块 的材质
        simpleBlockWithItem(ModBlocks.INFUSION_TABLE.get(), cubeAll(ModBlocks.INFUSION_TABLE.get()));
    }
}