package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin {
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    private void stasis$onNeighborUpdate(BlockPos pos, net.minecraft.block.Block block, BlockPos sourcePos, CallbackInfo ci) {
        World self = (World) (Object) this;
        BlockEntity be = self.getBlockEntity(pos);
        if (be != null) {
            BlockEntityScheduler.markActive(be);
        }
    }
}
