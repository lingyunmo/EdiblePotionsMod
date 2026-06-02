package cn.bzlom.block.entity;

import cn.bzlom.menu.InfusionTableMenu;
import cn.bzlom.registry.ModBlockEntities;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InfusionTableBlockEntity extends BlockEntity implements ExtendedMenuProvider, WorldlyContainer {

    // 缓存 foodId_potionId → Item 的映射，避免每次 tick 都查询注册表
    private static final Map<String, Item> RECIPE_CACHE = new HashMap<>();

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};

    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

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

        // 红石信号暂停注能（拉杆/按钮控制启停）
        if (level.hasNeighborSignal(pos)) return;

        boolean hadRecipe = hasRecipe(entity);

        if (hadRecipe) {
            entity.progress++;
            setChanged(level, pos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity, level, pos);
            }
        } else if (entity.progress > 0) {
            entity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static boolean hasRecipe(InfusionTableBlockEntity entity) {
        ItemStack foodStack = entity.items.get(0);
        ItemStack potionStack = entity.items.get(1);
        ItemStack outputStack = entity.items.get(2);

        if (foodStack.isEmpty() || potionStack.isEmpty()) return false;

        Item resultItem = findResultItem(foodStack, potionStack);
        if (resultItem == null) return false;

        return outputStack.isEmpty() ||
                (outputStack.getItem() == resultItem && outputStack.getCount() < outputStack.getMaxStackSize());
    }

    private static void craftItem(InfusionTableBlockEntity entity, Level level, BlockPos pos) {
        ItemStack foodStack = entity.items.get(0);
        ItemStack potionStack = entity.items.get(1);
        ItemStack outputStack = entity.items.get(2);

        Item resultItem = findResultItem(foodStack, potionStack);
        if (resultItem != null) {
            foodStack.shrink(1);

            // 返还对应的空瓶（原版所有药水类型都用玻璃瓶）
            entity.items.set(1, new ItemStack(Items.GLASS_BOTTLE));

            // 注能食物自身已编码递送方式（构造函数传入 Delivery），无需额外 NBT
            ItemStack result = new ItemStack(resultItem, 1);

            if (outputStack.isEmpty()) {
                entity.items.set(2, result);
            } else {
                outputStack.grow(1);
            }
            entity.resetProgress();

            entity.setChanged();
            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static Item findResultItem(ItemStack food, ItemStack potion) {
        String foodId = BuiltInRegistries.ITEM.getKey(food.getItem()).getPath();
        var potionType = PotionUtils.getPotion(potion);
        if (potionType == Potions.EMPTY) return null;
        String potionId = BuiltInRegistries.POTION.getKey(potionType).getPath();

        // 根据药水物品类型确定变体后缀（匹配 ModItems 中的三种注册 ID）
        Item potionItem = potion.getItem();
        String variant = potionItem == Items.SPLASH_POTION ? "_splash"
                : potionItem == Items.LINGERING_POTION ? "_lingering"
                : "";

        String cacheKey = foodId + "_" + potionId + variant;

        if (RECIPE_CACHE.containsKey(cacheKey)) {
            return RECIPE_CACHE.get(cacheKey);
        }

        ResourceLocation targetRl = new ResourceLocation("ediblepotions", cacheKey);
        Item result = BuiltInRegistries.ITEM.containsKey(targetRl)
                ? BuiltInRegistries.ITEM.get(targetRl)
                : null;
        RECIPE_CACHE.put(cacheKey, result);
        return result;
    }

    // --- 漏斗自动化接口 (WorldlyContainer) ---

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        return switch (side) {
            case DOWN -> SLOTS_FOR_DOWN;
            case UP -> SLOTS_FOR_UP;
            default -> SLOTS_FOR_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if (index == 2) return false;
        if (index == 1) return isPotion(itemStack);
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == 2) return true;
        return index == 1 && stack.getItem() == Items.GLASS_BOTTLE;
    }

    private boolean isPotion(ItemStack stack) {
        return stack.getItem() == Items.POTION ||
                stack.getItem() == Items.SPLASH_POTION ||
                stack.getItem() == Items.LINGERING_POTION;
    }

    // --- 基础容器接口 (Container) ---

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
        ContainerHelper.saveAllItems(tag, items);
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
        return new InfusionTableMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }
}
