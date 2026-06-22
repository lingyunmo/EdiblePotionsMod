package cn.bzlom.ediblepotions.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AOP 切面：拦截玩家死亡 / 重生流程，保留注能食物来源的药水效果。
 *
 * 设计思想（跨平台数据桥接）：
 * 原版死亡时 destroy() 清除效果，restoreFrom() 恢复新实体，
 * 两者是不同实体实例且中间没有共同的 NBT 载体。这里用静态 Map
 * 以 UUID 为键在 die() → restoreFrom() 之间传递效果快照，
 * 无需依赖 Fabric 的 getPersistentData() 或 Forge 的 Capability，
 * 同一份代码在双平台行为一致。
 *
 * 生命周期：
 *   die() → 快照效果存入 Map
 *   ├── 复活: restoreFrom() → 取出恢复，移除条目
 *   └── 断线: disconnect() → 移除残留条目，防止泄漏
 *   （单人游戏退出世界时 disconnect() 正常触发，无需额外服务端停止清理）
 *
 * 切入目标 #1：ServerPlayer.die(DamageSource)
 *   时机 @HEAD：原版清除效果之前序列化所有活跃效果
 *
 * 切入目标 #2：ServerPlayer.restoreFrom(ServerPlayer, boolean)
 *   时机 @TAIL：原版恢复数据后重新施加效果
 *
 * 切入目标 #3：ServerPlayer.disconnect()
 *   时机 @HEAD：玩家死亡后直接退出或退出世界时清除残留条目
 *
 * 横切关注点：死亡时效果持久化
 * 核心逻辑：原版死亡 / 重生处理 ← 不受影响
 */
@Mixin(value = ServerPlayer.class, priority = 900)
public abstract class DeathEffectMixin {

    @Unique
    private static final Map<UUID, ListTag> PENDING_EFFECTS = new HashMap<>();

    /**
     * 死亡前：快照所有活跃效果存入静态 Map。
     */
    @Inject(method = "die", at = @At("HEAD"))
    private void onDie(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (self.level().isClientSide) return;

        var effects = self.getActiveEffects();
        if (effects.isEmpty()) return;

        ListTag list = new ListTag();
        for (MobEffectInstance effect : effects) {
            if (!effect.isVisible()) continue; // 只保留可见效果（跳过信标等环境效果）
            list.add(effect.save(new CompoundTag()));
        }

        if (!list.isEmpty()) {
            PENDING_EFFECTS.put(self.getUUID(), list);
        }
    }

    /**
     * 重生后：从 Map 取出效果快照，重新施加到新玩家实体。
     * 使用 oldPlayer.getUUID() 作为键（oldPlayer 是死亡前的实体引用）。
     */
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onRestoreFrom(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (self.level().isClientSide) return;

        ListTag list = PENDING_EFFECTS.remove(oldPlayer.getUUID());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                MobEffectInstance instance = MobEffectInstance.load(list.getCompound(i));
                if (instance != null) {
                    // 死亡后恢复的效果持续时间上限 3 分钟（3600 ticks），
                    // 取原始时间与上限的较小值，避免死亡前吃的高级食物复活后效果过强
                    int cappedDuration = Math.min(instance.getDuration(), 3600);
                    MobEffectInstance capped = new MobEffectInstance(
                            instance.getEffect(),
                            cappedDuration,
                            instance.getAmplifier(),
                            instance.isAmbient(),
                            instance.isVisible(),
                            instance.showIcon()
                    );
                    self.addEffect(capped);
                }
            }
        }
    }

    /**
     * 玩家断线清理：死亡后不复活直接退出，移除残留条目防止泄漏。
     */
    @Inject(method = "disconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (self.level().isClientSide) return;
        PENDING_EFFECTS.remove(self.getUUID());
    }
}
