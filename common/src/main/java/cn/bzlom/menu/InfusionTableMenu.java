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
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class InfusionTableMenu extends AbstractContainerMenu {
    private static final int TE_INVENTORY_SLOT_COUNT = 3;
    private static final int SLOT_INPUT_FOOD = 0;
    private static final int SLOT_INPUT_POTION = 1;
    private static final int SLOT_OUTPUT = 2;

    private static final int VANILLA_FIRST_SLOT_INDEX = TE_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + 27;
    private static final int HOTBAR_SLOT_COUNT = 9;

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

        addDataSlots(data);

        // 输入槽 1 (食物)
        this.addSlot(new Slot(container, SLOT_INPUT_FOOD, 44, 35));
        // 输入槽 2 (药水)
        this.addSlot(new Slot(container, SLOT_INPUT_POTION, 62, 35));
        // 输出槽 (结果) — 不可放入物品
        this.addSlot(new Slot(container, SLOT_OUTPUT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
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
        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, ModBlocks.INFUSION_TABLE.get());
    }

    // --- Shift+点击 (Quick Move) 逻辑 ---
    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // 情况 A: 玩家点击了注能台里的槽位 (0, 1, 2) → 移入玩家背包
        if (index < VANILLA_FIRST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }
        // 情况 B: 玩家点击了自己背包里的东西 (3 - 38) → 尝试移入注能台
        else {
            // 1. 如果是药水 → 优先放入药水槽 (Index 1)
            if (isPotion(sourceStack)) {
                if (!moveItemStackTo(sourceStack, SLOT_INPUT_POTION, SLOT_INPUT_POTION + 1, false)) {
                    // 药水槽满了，尝试在背包内移动
                    if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, HOTBAR_FIRST_SLOT_INDEX, false)) {
                        if (!moveItemStackTo(sourceStack, HOTBAR_FIRST_SLOT_INDEX, HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
            // 2. 如果不是药水 → 尝试放入食物槽 (Index 0)
            else if (!moveItemStackTo(sourceStack, SLOT_INPUT_FOOD, SLOT_INPUT_FOOD + 1, false)) {
                // 食物槽放不了（满/不兼容），尝试在背包内排序移动
                if (index >= HOTBAR_FIRST_SLOT_INDEX) {
                    // 从快捷栏移到主背包
                    if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, HOTBAR_FIRST_SLOT_INDEX, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // 从主背包移到快捷栏
                    if (!moveItemStackTo(sourceStack, HOTBAR_FIRST_SLOT_INDEX, HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
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

    private boolean isPotion(ItemStack stack) {
        return stack.getItem() == Items.POTION ||
                stack.getItem() == Items.SPLASH_POTION ||
                stack.getItem() == Items.LINGERING_POTION;
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
