package cn.bzlom.block.entity;

import cn.bzlom.menu.InfusionTableMenu;
import cn.bzlom.registry.ModBlockEntities;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer; // 关键接口
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// 1. 实现 WorldlyContainer 接口
public class InfusionTableBlockEntity extends BlockEntity implements ExtendedMenuProvider, WorldlyContainer {

    // 定义自动化规则：哪个面对应哪个槽
    private static final int[] SLOTS_FOR_UP = new int[]{0};    // 上面只进食物
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1}; // 下面抽成品(2)和空瓶(1)
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};   // 侧面只进药水

    // 依然保留 items 列表来存数据，但这次我们自己管理它
    // 不再用 SimpleContainer 对象，而是直接让 BE 成为 Container
    private final net.minecraft.core.NonNullList<ItemStack> items = net.minecraft.core.NonNullList.withSize(3, ItemStack.EMPTY);

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public InfusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> InfusionTableBlockEntity.this.progress;
                    case 1 -> InfusionTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }
            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> InfusionTableBlockEntity.this.progress = value;
                    case 1 -> InfusionTableBlockEntity.this.maxProgress = value;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // --- 核心逻辑 Tick ---
    public static void tick(Level level, BlockPos pos, BlockState state, InfusionTableBlockEntity entity) {
        if (level.isClientSide) return;

        if (hasRecipe(entity)) {
            entity.progress++;
            setChanged(level, pos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static boolean hasRecipe(InfusionTableBlockEntity entity) {
        // 直接访问 items 列表
        ItemStack foodStack = entity.items.get(0);
        ItemStack potionStack = entity.items.get(1);
        ItemStack outputStack = entity.items.get(2);

        if (foodStack.isEmpty() || potionStack.isEmpty()) return false;

        Item resultItem = findResultItem(foodStack, potionStack);
        if (resultItem == null) return false;

        return outputStack.isEmpty() ||
                (outputStack.getItem() == resultItem && outputStack.getCount() < outputStack.getMaxStackSize());
    }

    private static void craftItem(InfusionTableBlockEntity entity) {
        ItemStack foodStack = entity.items.get(0);
        ItemStack potionStack = entity.items.get(1);
        ItemStack outputStack = entity.items.get(2);

        Item resultItem = findResultItem(foodStack, potionStack);
        if (resultItem != null) {
            foodStack.shrink(1);
            entity.items.set(1, new ItemStack(Items.GLASS_BOTTLE)); // 药水变空瓶

            if (outputStack.isEmpty()) {
                entity.items.set(2, new ItemStack(resultItem, 1));
            } else {
                outputStack.grow(1);
            }
            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static Item findResultItem(ItemStack food, ItemStack potion) {
        String foodId = BuiltInRegistries.ITEM.getKey(food.getItem()).getPath();
        var potionType = PotionUtils.getPotion(potion);
        if (potionType == net.minecraft.world.item.alchemy.Potions.EMPTY) return null;
        String potionId = BuiltInRegistries.POTION.getKey(potionType).getPath();

        ResourceLocation targetRl = new ResourceLocation("ediblepotions", foodId + "_" + potionId);
        if (BuiltInRegistries.ITEM.containsKey(targetRl)) {
            return BuiltInRegistries.ITEM.get(targetRl);
        }
        return null;
    }

    // --- 漏斗自动化接口 (WorldlyContainer) ---

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        // 规则：
        // 1. 只有输入槽 (0, 1) 可以放入物品
        // 2. 只有方向匹配时才允许放入 (getSlotsForFace 已经过滤了方向，这里做二次校验)
        // 3. 只有合法的物品才能放入 (比如药水槽只能放药水)
        if (index == 2) return false; // 输出槽不能塞东西进去

        if (index == 1) { // 药水槽
            return isPotion(itemStack);
        }
        return true; // 食物槽暂时允许放任何东西 (或者你可以加个 isFood 判断)
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        // 规则：只要是输出槽的东西，或者药水槽里的空瓶子，就能抽走
        if (index == 2) return true;
        return index == 1 && stack.getItem() == Items.GLASS_BOTTLE;
    }

    private boolean isPotion(ItemStack stack) {
        return stack.getItem() == Items.POTION ||
                stack.getItem() == Items.SPLASH_POTION ||
                stack.getItem() == Items.LINGERING_POTION;
    }

    // --- 基础容器接口 (Container) 实现 ---
    // 因为删除了 SimpleContainer，BlockEntity 自己变成了 Container

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : items) {
            if (!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    // --- NBT 保存与读取 ---

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items); // 现在可以直接用了，因为 items 是 public 访问或者包内访问
        tag.putInt("infusion_progress", progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        progress = tag.getInt("infusion_progress");
    }

    // --- MenuProvider ---

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.ediblepotions.infusion_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        // 注意：这里传 this，因为 BlockEntity 现在自己就是 Container
        return new InfusionTableMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }
}