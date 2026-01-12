package cn.bzlom.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils; // 核心工具类
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
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (!level.isClientSide) {
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
    //原生级命名逻辑 (Swiftness II Apple / 迅捷 II 苹果)
    // ---------------------------------------------------------
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        // 安全检查
        if (this.potion.getEffects().isEmpty()) {
            return super.getName(stack);
        }

        // 1. 获取药水里的核心效果数据
        MobEffectInstance effectInstance = this.potion.getEffects().get(0);

        // 2. 获取效果的基础名称 (自动翻译)
        MutableComponent effectName = (MutableComponent) effectInstance.getEffect().getDisplayName();

        // 3. 动态判断等级，添加罗马数字 (II, III, IV...)
        // getAmplifier() 返回 0 代表等级 I，返回 1 代表等级 II
        if (effectInstance.getAmplifier() > 0) {
            // "potion.potency.1" -> "II"
            // "potion.potency.2" -> "III"
            // 这种写法能自动适应所有语言包
            Component amplifier = Component.translatable("potion.potency." + effectInstance.getAmplifier());
            effectName.append(" ").append(amplifier);
        }

        // 4. 获取原版食物的名字 (自动翻译)
        Component foodName = this.baseFood.getName(new ItemStack(this.baseFood));

        // 5. 最终拼接： "迅捷 II 熟猪排"
        return Component.translatable("%s %s", effectName, foodName);
    }

    // ---------------------------------------------------------
    // 升级 2：详细数据提示 (显示：速度 II (1:30))
    // ---------------------------------------------------------
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        // 调用原版 PotionUtils
        // 它会自动处理：
        // 1. 效果名称 (红色字体)
        // 2. 等级 (II, III)
        // 3. 时间 (3:00, 8:00)
        // 4. 负面效果自动变红色
        PotionUtils.addPotionTooltip(this.potion.getEffects(), tooltipComponents, 1.0F);

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public Item getBaseFood() {
        return baseFood;
    }
}