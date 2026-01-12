package cn.bzlom.registry;

import cn.bzlom.EdiblePotionsMod;
import cn.bzlom.block.InfusionTableBlock;
import cn.bzlom.block.entity.InfusionTableBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(EdiblePotionsMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<InfusionTableBlockEntity>> INFUSION_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("infusion_table_be", () ->
                    BlockEntityType.Builder.of(InfusionTableBlockEntity::new, ModBlocks.INFUSION_TABLE.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}