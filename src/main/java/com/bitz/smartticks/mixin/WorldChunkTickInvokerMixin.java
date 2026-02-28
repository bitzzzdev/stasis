package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$DirectBlockEntityTickInvoker")
public abstract class WorldChunkTickInvokerMixin {
    @Shadow @Final private BlockEntity blockEntity;
    @Shadow @Final private BlockEntityTicker<BlockEntity> ticker;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void stasis$gateTick(CallbackInfo ci) {
        if (blockEntity == null || blockEntity.getWorld() == null || blockEntity.getWorld().isClient()) {
            return;
        }

        if (!(blockEntity.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        if (!BlockEntityScheduler.shouldTick(serverWorld, blockEntity.getPos(), blockEntity.getCachedState(), blockEntity)) {
            ci.cancel();
        }
    }
}
