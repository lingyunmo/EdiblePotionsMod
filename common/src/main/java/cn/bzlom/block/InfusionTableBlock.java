package cn.bzlom.block;

import cn.bzlom.block.entity.InfusionTableBlockEntity;
import dev.architectury.registry.menu.MenuRegistry; // 导入菜单注册器
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class InfusionTableBlock extends BaseEntityBlock {

    public InfusionTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusionTableBlockEntity(pos, state);
    }

    // --- 修改这里 ---
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            // 1. 获取方块实体
            BlockEntity be = level.getBlockEntity(pos);

            // 2. 检查它是否是我们的实体
            if (be instanceof InfusionTableBlockEntity) {
                // 3. 使用 Architectury 打开扩展菜单
                // openExtendedMenu 会自动调用 saveExtraData 把坐标传过去
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (InfusionTableBlockEntity) be);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        // 注意：这里需要根据你的类名稍微调整
        return createTickerHelper(blockEntityType, cn.bzlom.registry.ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY.get(), InfusionTableBlockEntity::tick);
    }
}