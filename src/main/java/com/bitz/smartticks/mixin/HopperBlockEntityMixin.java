package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Inject(method = "setStack", at = @At("HEAD"), require = 0)
    private void stasis$onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }

    @Inject(method = "setTransferCooldown", at = @At("HEAD"), require = 0)
    private void stasis$onCooldownChanged(int transferCooldown, CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }

    @Inject(method = "markDirty", at = @At("HEAD"), require = 0)
    private void stasis$onMarkDirty(CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }
}
