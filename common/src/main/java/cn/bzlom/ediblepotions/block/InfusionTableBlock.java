package cn.bzlom.ediblepotions.block;

import cn.bzlom.ediblepotions.block.entity.InfusionTableBlockEntity;
import cn.bzlom.ediblepotions.registry.ModBlockEntities;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof InfusionTableBlockEntity) {
            MenuRegistry.openExtendedMenu((net.minecraft.server.level.ServerPlayer) player, (InfusionTableBlockEntity) be);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfusionTableBlockEntity infusionTable) {
                // 打破方块时掉落内部物品
                Containers.dropContents(level, pos, infusionTable);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof InfusionTableBlockEntity infusionTable) {
            // 比较器输出: 基于注能进度 0-15
            int progress = infusionTable.getProgress();
            int maxProgress = infusionTable.getMaxProgress();
            if (maxProgress > 0) {
                return progress * 15 / maxProgress;
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return createTickerHelper(blockEntityType, ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY.get(), InfusionTableBlockEntity::tick);
    }
}
