package cn.bzlom.mixin;

import cn.bzlom.item.InfusedFoodItem;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * AOP 切面：拦截原版进食粒子系统，为注能食物注入药水颜色的粒子特效。
 *
 * 切入目标：LivingEntity.addEatEffect(ItemStack, Level, LivingEntity)
 * 织入时机：@TAIL（原版粒子生成完毕后）
 *
 * 横切关注点：视觉效果（粒子颜色 / 数量 / 范围）
 * 核心逻辑：原版食物消耗 / 效果施加 ← 不受影响
 *
 * 此 Mixin 在 Forge 和 Fabric 上使用完全相同的字节码，
 * 构建时由 Architectury Transformer 自动重映射注解。
 */
@Mixin(LivingEntity.class)
public abstract class EatingEffectMixin {

    @Inject(method = "addEatEffect", at = @At("TAIL"))
    private void onAddEatEffect(ItemStack stack, Level level, LivingEntity entity, CallbackInfo ci) {
        if (level.isClientSide) return;
        if (!(stack.getItem() instanceof InfusedFoodItem food)) return;

        // 获取药水颜色，粒子数量与效果数量成正比
        var effects = food.getPotion().getEffects();
        if (effects.isEmpty()) return;

        int color = PotionUtils.getColor(food.getPotion());
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        int particleCount = 12 + effects.size() * 4;

        for (int i = 0; i < particleCount; i++) {
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 0.8;
            double y = entity.getY() + entity.getRandom().nextDouble() * 1.8;
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 0.8;

            level.addParticle(
                    new DustParticleOptions(new Vector3f(r, g, b), 1.2F),
                    x, y, z,
                    0.0, 0.05, 0.0
            );
        }
    }
}
