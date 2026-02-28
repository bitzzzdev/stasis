package com.bitz.smartticks.mixin;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin {
    @Inject(method = "setStack", at = @At("HEAD"), require = 0)
    private void stasis$onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        BlockEntityScheduler.markActive((BlockEntity) (Object) this);
    }
}
