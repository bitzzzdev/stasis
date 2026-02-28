package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import net.minecraft.block.entity.ComparatorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComparatorBlockEntity.class)
public abstract class ComparatorBlockEntityMixin {
    @Inject(method = "setOutputSignal", at = @At("HEAD"), require = 0)
    private void stasis$onOutputChanged(int outputSignal, CallbackInfo ci) {
        BlockEntityScheduler.markActive((ComparatorBlockEntity) (Object) this);
    }

    @Inject(method = "markDirty", at = @At("HEAD"), require = 0)
    private void stasis$onMarkDirty(CallbackInfo ci) {
        BlockEntityScheduler.markActive((ComparatorBlockEntity) (Object) this);
    }
}
