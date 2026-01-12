package cn.bzlom.menu;

import cn.bzlom.registry.ModBlocks;
import cn.bzlom.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfusionTableMenu extends AbstractContainerMenu {
    // 定义常量
    private static final int TE_INVENTORY_SLOT_COUNT = 3; // 自定义方块槽位数量
    private static final int SLOT_INPUT_FOOD = 0;
    private static final int SLOT_INPUT_POTION = 1;

    // 玩家背包相关常量
    private static final int VANILLA_FIRST_SLOT_INDEX = TE_INVENTORY_SLOT_COUNT; // 3
    private static final int VANILLA_SLOT_COUNT = 36; // 27背包 + 9快捷栏

    private final ContainerData data;
    private final ContainerLevelAccess levelAccess;

    // 客户端构造函数
    public InfusionTableMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, new SimpleContainer(3), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    // 服务端构造函数
    public InfusionTableMenu(int id, Inventory inv, Container container, ContainerData data, ContainerLevelAccess access) {
        super(ModMenuTypes.INFUSION_TABLE_MENU.get(), id);
        checkContainerSize(container, 3);
        this.data = data;
        this.levelAccess = access;
        Level level = inv.player.level();

        addDataSlots(data);

        // 输入槽 1 (食物)
        this.addSlot(new Slot(container, 0, 44, 35));
        // 输入槽 2 (药水)
        this.addSlot(new Slot(container, 1, 62, 35));
        // 输出槽 (结果)
        this.addSlot(new Slot(container, 2, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24; // 箭头的总像素宽度

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, ModBlocks.INFUSION_TABLE.get());
    }

    // --- 核心修复：Shift+点击 (Quick Move) 逻辑 ---
    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // 情况 A: 玩家点击了注能台里的槽位 (0, 1, 2) -> 尝试移入玩家背包
        if (index < VANILLA_FIRST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }
        // 情况 B: 玩家点击了自己背包里的东西 (3 - 38) -> 尝试移入注能台
        else {

            // 1. 如果是药水 -> 优先尝试放入药水槽 (Index 1)
            if (isPotion(sourceStack)) {
                if (!moveItemStackTo(sourceStack, SLOT_INPUT_POTION, SLOT_INPUT_POTION + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // 2. 如果不是药水，或者药水槽满了 -> 尝试放入食物槽 (Index 0)
            else {
                if (!moveItemStackTo(sourceStack, SLOT_INPUT_FOOD, SLOT_INPUT_FOOD + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    // 辅助判断：检查是否是药水物品 (用于智能放入)
    private boolean isPotion(ItemStack stack) {
        // 判断是不是原版药水
        return stack.getItem() == net.minecraft.world.item.Items.POTION ||
                stack.getItem() == net.minecraft.world.item.Items.SPLASH_POTION ||
                stack.getItem() == net.minecraft.world.item.Items.LINGERING_POTION;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}