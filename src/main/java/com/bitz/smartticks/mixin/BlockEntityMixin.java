package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import com.bitz.smartticks.scheduler.SchedulerAwareBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements SchedulerAwareBlockEntity {
    @Unique private long stasis$lastActiveGameTime;
    @Unique private boolean stasis$active = true;
    @Unique private int stasis$tickInterval = 1;
    @Unique private int stasis$lastSnapshotHash = Integer.MIN_VALUE;
    @Unique private int stasis$lastComparatorOutput = Integer.MIN_VALUE;
    @Unique private int stasis$lastPowerCache = Integer.MIN_VALUE;

    @Override public long stasis$getLastActiveGameTime() { return stasis$lastActiveGameTime; }
    @Override public void stasis$setLastActiveGameTime(long gameTime) { stasis$lastActiveGameTime = gameTime; }
    @Override public boolean stasis$isActive() { return stasis$active; }
    @Override public void stasis$setActive(boolean active) { stasis$active = active; }
    @Override public int stasis$getTickInterval() { return stasis$tickInterval; }
    @Override public void stasis$setTickInterval(int interval) { stasis$tickInterval = interval; }
    @Override public int stasis$getLastSnapshotHash() { return stasis$lastSnapshotHash; }
    @Override public void stasis$setLastSnapshotHash(int hash) { stasis$lastSnapshotHash = hash; }
    @Override public int stasis$getLastComparatorOutput() { return stasis$lastComparatorOutput; }
    @Override public void stasis$setLastComparatorOutput(int value) { stasis$lastComparatorOutput = value; }
    @Override public int stasis$getLastPowerCache() { return stasis$lastPowerCache; }
    @Override public void stasis$setLastPowerCache(int power) { stasis$lastPowerCache = power; }

    @Inject(method = "markDirty", at = @At("HEAD"))
    private void stasis$onMarkDirty(CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }

    @Inject(method = "setCachedState", at = @At("HEAD"))
    private void stasis$onSetCachedState(BlockState state, CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }

    @Inject(method = "setPos", at = @At("HEAD"), require = 0)
    private void stasis$onSetPos(BlockPos pos, CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }
}
