package cn.bzlom.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusedFoodItem extends Item {

    /** 药水递送方式：普通饮用 / 喷溅溅射 / 滞留云 */
    public enum Delivery {
        REGULAR,
        SPLASH,
        LINGERING
    }

    private final Potion potion;
    private final Item baseFood;
    private final Delivery delivery;

    public InfusedFoodItem(Item baseFood, Potion potion, Delivery delivery) {
        super(new Properties()
                .food(baseFood.getFoodProperties())
                .stacksTo(64));
        this.baseFood = baseFood;
        this.potion = potion;
        this.delivery = delivery;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        int countBefore = stack.getCount();
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        if (!level.isClientSide && result.getCount() < countBefore) {
            List<MobEffectInstance> effects = potion.getEffects();

            // 基础效果 — 总是施加给食用者
            for (MobEffectInstance effectInstance : effects) {
                livingEntity.addEffect(new MobEffectInstance(effectInstance));
            }

            // 喷溅型 — 效果溅射到附近 3 格内所有生物
            if (delivery == Delivery.SPLASH) {
                AABB area = livingEntity.getBoundingBox().inflate(3.0, 1.5, 3.0);
                List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area,
                        e -> e != livingEntity && e.isAlive());
                for (LivingEntity target : nearby) {
                    for (MobEffectInstance effect : effects) {
                        target.addEffect(new MobEffectInstance(effect));
                    }
                }
            }

            // 滞留型 — 原地留下药水云
            if (delivery == Delivery.LINGERING) {
                AreaEffectCloud cloud = new AreaEffectCloud(level,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                cloud.setOwner(livingEntity);
                cloud.setRadius(3.0F);
                cloud.setRadiusOnUse(-0.5F);
                cloud.setWaitTime(10);
                cloud.setDuration(cloud.getDuration() / 2);
                cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
                for (MobEffectInstance effect : effects) {
                    cloud.addEffect(new MobEffectInstance(effect));
                }
                level.addFreshEntity(cloud);
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (this.potion.getEffects().isEmpty()) {
            return super.getName(stack);
        }

        // 1. 药水效果名 + 等级
        MobEffectInstance effectInstance = this.potion.getEffects().get(0);
        MutableComponent effectName = effectInstance.getEffect().getDisplayName().copy();
        if (effectInstance.getAmplifier() > 0) {
            Component amplifier = Component.translatable("potion.potency." + effectInstance.getAmplifier());
            effectName.append(" ").append(amplifier);
        }

        // 2. 食物名称
        Component foodName = this.baseFood.getName(new ItemStack(this.baseFood));

        // 3. 药水类型前缀（喷溅/滞留）
        String prefixKey = switch (delivery) {
            case SPLASH -> "item.ediblepotions.splash_prefix";
            case LINGERING -> "item.ediblepotions.lingering_prefix";
            case REGULAR -> null;
        };

        if (prefixKey != null) {
            return Component.translatable("item.ediblepotions.infused_food.name.prefixed",
                    Component.translatable(prefixKey), effectName, foodName);
        }

        return Component.translatable("item.ediblepotions.infused_food.name", effectName, foodName);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        // 药水类型提示
        if (delivery == Delivery.SPLASH) {
            tooltipComponents.add(Component.translatable("item.ediblepotions.splash_prefix")
                    .withStyle(ChatFormatting.AQUA));
        } else if (delivery == Delivery.LINGERING) {
            tooltipComponents.add(Component.translatable("item.ediblepotions.lingering_prefix")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        PotionUtils.addPotionTooltip(this.potion.getEffects(), tooltipComponents, 1.0F);
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public Item getBaseFood() {
        return baseFood;
    }

    public Potion getPotion() {
        return potion;
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
