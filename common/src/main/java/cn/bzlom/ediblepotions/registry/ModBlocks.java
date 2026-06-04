package cn.bzlom.ediblepotions.registry;

import cn.bzlom.ediblepotions.EdiblePotionsMod;
import cn.bzlom.ediblepotions.block.InfusionTableBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.ITEM);

    // 注册方块：注能台
    // copy(Blocks.STONE) 表示继承石头的硬度、抗爆属性
    public static final RegistrySupplier<Block> INFUSION_TABLE = BLOCKS.register("infusion_table",
            () -> new InfusionTableBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)));

    // 注册方块对应的物品 (BlockItem)，否则你拿不在手里
    public static final RegistrySupplier<Item> INFUSION_TABLE_ITEM = ITEMS.register("infusion_table",
            () -> new BlockItem(INFUSION_TABLE.get(), new Item.Properties().arch$tab(ModCreativeTabs.MAIN_TAB)));

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }
}