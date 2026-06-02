package cn.bzlom.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusedFoodItem extends Item {
    private final Potion potion;
    private final Item baseFood;

    public InfusedFoodItem(Item baseFood, Potion potion) {
        super(new Properties()
                .food(baseFood.getFoodProperties())
                .stacksTo(64));
        this.baseFood = baseFood;
        this.potion = potion;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        // 记录食用前的数量，用于判断是否真正消耗了物品
        int countBefore = stack.getCount();
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        // 只有真正消耗了食物（非创造模式）才施加药水效果
        if (!level.isClientSide && result.getCount() < countBefore) {
            for (MobEffectInstance effectInstance : potion.getEffects()) {
                livingEntity.addEffect(new MobEffectInstance(effectInstance));
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    // ---------------------------------------------------------
    // 原生级命名逻辑 (Swiftness II Apple / 迅捷 II 苹果)
    // ---------------------------------------------------------
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (this.potion.getEffects().isEmpty()) {
            return super.getName(stack);
        }

        // 1. 获取药水里的核心效果数据
        MobEffectInstance effectInstance = this.potion.getEffects().get(0);

        // 2. 获取效果的基础名称 (自动翻译)
        MutableComponent effectName = effectInstance.getEffect().getDisplayName().copy();

        // 3. 动态判断等级，添加罗马数字 (II, III, IV...)
        if (effectInstance.getAmplifier() > 0) {
            Component amplifier = Component.translatable("potion.potency." + effectInstance.getAmplifier());
            effectName.append(" ").append(amplifier);
        }

        // 4. 获取原版食物的名字 (自动翻译)
        Component foodName = this.baseFood.getName(new ItemStack(this.baseFood));

        // 5. 使用命名空间化的翻译键，允许各语言自定义语序
        return Component.translatable("item.ediblepotions.infused_food.name", effectName, foodName);
    }

    // ---------------------------------------------------------
    // 升级 2：详细数据提示 (显示：速度 II (1:30))
    // ---------------------------------------------------------
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        PotionUtils.addPotionTooltip(this.potion.getEffects(), tooltipComponents, 1.0F);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public Item getBaseFood() {
        return baseFood;
    }
}
